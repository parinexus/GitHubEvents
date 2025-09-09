package parinexus.sample.githubevents.data.repository.sync

import javax.inject.Inject
import kotlinx.coroutines.*
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.EventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.port.TransactionRunner
import parinexus.sample.githubevents.data.repository.util.DEFAULT_PAGE_SIZE
import parinexus.sample.githubevents.data.repository.util.DEFAULT_POLL_INTERVAL_MS

class EventsPoller @Inject constructor(
    private val remote: EventsRemoteDataSource,
    private val local: EventsLocalDataSource,
    private val tx: TransactionRunner
) {
    private var job: Job? = null

    fun start(
        scope: CoroutineScope,
        startImmediately: Boolean = true
    ) {
        if (job?.isActive == true) return

        job = scope.launch(Dispatchers.IO + SupervisorJob()) {
            val existingLastSeen = local.latestCreatedAt()
            var lastSeen: Long = existingLastSeen ?: Long.MIN_VALUE
            if (existingLastSeen != null && !startImmediately) delay(DEFAULT_POLL_INTERVAL_MS)

            while (isActive) {
                try {
                    val res = remote.getEvents(limit = DEFAULT_PAGE_SIZE, page = 1)
                    when (res.code) {
                        304 -> Unit
                        in 200..299 -> {
                            val candidates: List<RepoEvent> =
                                res.items.filter { it.createdAtEpochMillis >= lastSeen }

                            val newOnes = mutableListOf<RepoEvent>()
                            for (e in candidates) {
                                if (local.getById(e.id) == null) newOnes += e
                            }

                            if (newOnes.isNotEmpty()) {
                                tx { local.upsertAll(newOnes) }
                                lastSeen = maxOf(
                                    lastSeen,
                                    newOnes.maxOf { it.createdAtEpochMillis }
                                )
                            }
                        }

                        else -> {}
                    }
                } catch (ce: CancellationException) {
                    break
                } catch (_: Throwable) {
                }

                delay(DEFAULT_POLL_INTERVAL_MS)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
    }
}