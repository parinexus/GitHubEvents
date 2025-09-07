package parinexus.sample.githubevents.features.eventdetailapi.robots

import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.RepoModel
import parinexus.sample.githubevents.features.eventdetailapi.item.UserActor
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.features.eventdetailapi.item.UserGithubRepository
import parinexus.sample.githubevents.features.eventdetailapi.mapper.toDetailActor
import parinexus.sample.githubevents.features.eventdetailapi.mapper.toDetailGithubRepository
import parinexus.sample.githubevents.features.eventdetailapi.mapper.toDetailsView
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals
import kotlin.test.assertNull

class EventDetailItemMapperRobot : BaseRobot() {

    private lateinit var domainActor: Actor
    private var domainRepo: RepoModel? = null
    private lateinit var domainEvent: Event

    private var userActor: UserActor? = null
    private var userRepo: UserGithubRepository? = null
    private var userEvent: UserEvent? = null

    fun aFakeActor(login: String = "octo", avatar: String = "https://img/a.png") = apply {
        domainActor = Actor(login = login, avatarUrl = avatar)
    }

    fun aFakeRepo(name: String = "repo", url: String = "https://github.com/x/repo") = apply {
        domainRepo = RepoModel(name = name, url = url)
    }

    fun aFakeEvent(
        id: String = "1",
        login: String = "octo",
        avatar: String = "https://img/a.png",
        repoName: String? = "repo",
        repoUrl: String? = "https://github.com/x/repo",
        createdAt: String = "2024-01-01T00:00:00Z"
    ) = apply {
        val actor = Actor(login = login, avatarUrl = avatar)
        val repo = if (repoName != null && repoUrl != null) RepoModel(name = repoName, url = repoUrl) else null
        domainEvent = Event(
            id = id,
            actor = actor,
            repo = repo,
            type = "",
            createdAt = createdAt
        )
    }

    fun mapActor() = apply {
        userActor = domainActor.toDetailActor()
    }

    fun mapRepo() = apply {
        requireNotNull(domainRepo) { "Call aFakeRepo() first" }
        userRepo = domainRepo!!.toDetailGithubRepository()
    }

    fun mapEvent() = apply {
        userEvent = domainEvent.toDetailsView()
    }

    fun userActorIs(expected: UserActor) = apply {
        assertEquals(expected, userActor)
    }

    fun userRepoIs(expected: UserGithubRepository) = apply {
        assertEquals(expected, userRepo)
    }

    fun userEventIs(expected: UserEvent) = apply {
        assertEquals(expected, userEvent)
    }

    fun userEventRepoIsNull() = apply {
        assertNull(userEvent?.repo)
    }
}