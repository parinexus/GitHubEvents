package parinexus.sample.githubevents.data.repository.robots

import androidx.paging.testing.asSnapshot
import parinexus.sample.githubevents.data.repository.GitHubRepositoryImpl
import parinexus.sample.githubevents.data.repository.ImmediateTransactionRunner
import parinexus.sample.githubevents.data.repository.datasource.FakeEventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.FakeEventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.datasource.FakeRemoteKeysLocalDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys
import parinexus.sample.githubevents.data.repository.port.RemoteFetchResult
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals

class GitHubRepositoryImplRobot: BaseRobot(){

    private val remote = FakeEventsRemoteDataSource()
    private val local  = FakeEventsLocalDataSource()
    private val keys   = FakeRemoteKeysLocalDataSource()
    private val tx     = ImmediateTransactionRunner()

    private val repository = GitHubRepositoryImpl(
        eventsRemoteDataSource = remote,
        eventsLocalDataSource  = local,
        remoteKeysLocalDataSource = keys,
        transactionRunner = tx
    )

    var snapshot: List<Event> = emptyList(); private set

    fun repoEvent(id: String, createdAt: Long, login: String) = RepoEvent(
        id = id,
        type = "PushEvent",
        actorLogin = login,
        actorAvatarUrl = "",
        repoName = null,
        repoUrl = null,
        createdAtEpochMillis = createdAt
    )

    fun seedRemote(items: List<RepoEvent>) {
        remote.next = RemoteFetchResult(code = 200, items = items, linkHeader = null)
    }

    suspend fun loadSnapshot(pageSize: Int = 30) {
        mediatorRefresh(page = 1, pageSize = pageSize)
        snapshot = repository.getEvents().asSnapshot()
    }

    fun assertPageIds(expected: List<String>) {
        assertEquals(expected, snapshot.map { it.id })
    }

    fun assertFirstActor(expected: String) {
        val first = snapshot.firstOrNull() ?: error("Empty snapshot")
        assertEquals(expected, first.actor.login)
    }

    fun assertPageSize(expected: Int) {
        assertEquals(expected, snapshot.size)
    }

    suspend fun assertGetByIdLogin(id: String, expectedLogin: String) {
        val domain = repository.getEventById(id) ?: error("Event $id not found")
        assertEquals(expectedLogin, domain.actor.login)
    }

    suspend fun assertGetByIdIsNull(id: String) {
        val domain = repository.getEventById(id)
        assertEquals(null, domain)
    }

    private suspend fun mediatorRefresh(page: Int, pageSize: Int) {
        val res = remote.getEvents(limit = pageSize, page = page)
        if (res.code in 200..299 && res.items.isNotEmpty()) {
            tx {
                keys.clear()
                local.clearAll()
                val prevKey: Int? = null
                val nextKey: Int? = null
                val keyModels = res.items.map { RepoRemoteKeys(eventId = it.id, prevKey = prevKey, nextKey = nextKey) }
                keys.insertAll(keyModels)
                local.upsertAll(res.items)
            }
        }
    }
}