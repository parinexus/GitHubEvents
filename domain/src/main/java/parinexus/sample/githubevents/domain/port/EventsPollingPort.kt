package parinexus.sample.githubevents.domain.port

import kotlinx.coroutines.CoroutineScope

interface EventsPollingPort {
    fun start(
        scope: CoroutineScope,
        intervalMs: Long = 10_000L,
        pageSize: Int = 2,
        startImmediately: Boolean = true
    )

    fun stop()
}