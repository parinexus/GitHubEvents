package parinexus.sample.githubevents.data.repository.adapter

import kotlinx.coroutines.CoroutineScope
import parinexus.sample.githubevents.data.repository.sync.EventsPoller
import parinexus.sample.githubevents.domain.port.EventsPollingPort
import javax.inject.Inject

class EventsPollingAdapter @Inject constructor(
    private val poller: EventsPoller
) : EventsPollingPort {
    override fun start(
        scope: CoroutineScope,
        startImmediately: Boolean
    ) {
        poller.start(scope, startImmediately)
    }

    override fun stop() {
        poller.stop()
    }
}