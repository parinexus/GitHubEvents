package parinexus.sample.githubevents.features.searchapi.usecase

import kotlinx.coroutines.CoroutineScope
import parinexus.sample.githubevents.domain.port.EventsPollingPort
import javax.inject.Inject

class StartEventsPollingUseCase @Inject constructor(
    private val port: EventsPollingPort
) {
    operator fun invoke(
        scope: CoroutineScope,
        intervalMs: Long = 10_000L,
        pageSize: Int = 30,
        startImmediately: Boolean = true
    ) =
        port.start(scope, intervalMs, pageSize, startImmediately)
}