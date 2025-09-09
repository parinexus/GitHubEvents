package parinexus.sample.githubevents.features.searchapi.usecase

import kotlinx.coroutines.CoroutineScope
import parinexus.sample.githubevents.domain.port.EventsPollingPort
import javax.inject.Inject

class StartEventsPollingUseCase @Inject constructor(
    private val port: EventsPollingPort
) {
    operator fun invoke(
        scope: CoroutineScope,
        startImmediately: Boolean = true
    ) =
        port.start(scope, startImmediately)
}