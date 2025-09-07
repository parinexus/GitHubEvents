package parinexus.sample.githubevents.data.repository.robots

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import parinexus.sample.githubevents.data.repository.ImmediateTransactionRunner
import parinexus.sample.githubevents.data.repository.TestLocalWrapper
import parinexus.sample.githubevents.data.repository.datasource.FakeEventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.FakeEventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.port.RemoteFetchResult
import parinexus.sample.githubevents.data.repository.port.TransactionRunner
import parinexus.sample.githubevents.data.repository.sync.EventsPoller
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals

class EventsPollerRobot : BaseRobot() {

    private val remoteBase = FakeEventsRemoteDataSource()
    private val localBase  = FakeEventsLocalDataSource()
    private val local      = TestLocalWrapper(localBase)
    private val tx: TransactionRunner = ImmediateTransactionRunner()
    private val poller = EventsPoller(remote = remoteBase, local = local, tx = tx)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun repoEvent(id: String, createdAt: Long, login: String) = RepoEvent(
        id = id,
        type = "PushEvent",
        actorLogin = login,
        actorAvatarUrl = "",
        repoName = null,
        repoUrl = null,
        createdAtEpochMillis = createdAt
    )

    suspend fun seedLocal(vararg events: RepoEvent) { local.seed(*events) }

    fun seedRemote(code: Int, items: List<RepoEvent>) {
        remoteBase.next = RemoteFetchResult(code = code, items = items, linkHeader = null)
    }

    suspend fun startPollerAndWaitFirstInsert(intervalMs: Long = 10_000L) {
        local.firstUpsertSignal = CompletableDeferred()
        poller.start(scope = scope, intervalMs = intervalMs, pageSize = 30, startImmediately = true)
        local.firstUpsertSignal?.await()
    }

    suspend fun startPollerAndWaitSilently(intervalMs: Long = 10_000L) {
        poller.start(scope = scope, intervalMs = intervalMs, pageSize = 30, startImmediately = true)
        delayReal(25L)
    }

    fun stopPoller() = poller.stop()
    fun stopAndDispose() { poller.stop(); scope.cancel() }

    suspend fun assertLocalIdsByCreatedDesc(expected: List<String>) {
        assertEquals(expected, local.idsByCreatedDesc())
    }

    suspend fun assertLocalSize(expected: Int) {
        assertEquals(expected, local.size())
    }

    suspend fun delayReal(ms: Long) = delay(ms)
}