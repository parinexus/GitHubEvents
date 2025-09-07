package parinexus.sample.githubevents.domain.port

import kotlinx.coroutines.CoroutineScope

interface EventsPollingPort {
    fun start(scope: CoroutineScope, intervalMs: Long = 10_000L, pageSize: Int = 30)
    fun stop()
}