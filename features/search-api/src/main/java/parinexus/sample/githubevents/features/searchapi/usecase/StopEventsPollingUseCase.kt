package parinexus.sample.githubevents.features.searchapi.usecase

import parinexus.sample.githubevents.domain.port.EventsPollingPort
import javax.inject.Inject

class StopEventsPollingUseCase @Inject constructor(
    private val port: EventsPollingPort
) {
    operator fun invoke() = port.stop()
}