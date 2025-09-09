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

    private val local  = FakeEventsLocalDataSource()

    private val repository = GitHubRepositoryImpl(
        eventsLocalDataSource  = local,
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

    suspend fun seedLocal(items: List<RepoEvent>) {
        local.upsertAll(items)
    }

    suspend fun loadSnapshot() {
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

}