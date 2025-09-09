package parinexus.sample.githubevents.domain.port

import kotlinx.coroutines.CoroutineScope

interface EventsPollingPort {
    fun start(
        scope: CoroutineScope,
        startImmediately: Boolean = true
    )

    fun stop()
}