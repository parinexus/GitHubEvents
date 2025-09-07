package parinexus.sample.githubevents.data.repository.adapter

import kotlinx.coroutines.CoroutineScope
import parinexus.sample.githubevents.data.repository.sync.EventsPoller
import parinexus.sample.githubevents.domain.port.EventsPollingPort
import javax.inject.Inject

class EventsPollingAdapter @Inject constructor(
    private val poller: EventsPoller
) : EventsPollingPort {
    override fun start(scope: CoroutineScope, intervalMs: Long, pageSize: Int) {
        poller.start(scope, intervalMs, pageSize)
    }
    override fun stop() {
        poller.stop()
    }
}