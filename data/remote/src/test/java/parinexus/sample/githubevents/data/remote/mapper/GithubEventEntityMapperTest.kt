package parinexus.sample.githubevents.data.remote.mapper

import parinexus.sample.githubevents.libraries.test.BaseRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import junit.framework.TestCase.assertEquals
import org.junit.Test
import parinexus.sample.githubevents.data.remote.entity.Actor
import parinexus.sample.githubevents.data.remote.entity.EventType
import parinexus.sample.githubevents.data.remote.entity.GitHubEvent
import parinexus.sample.githubevents.data.remote.entity.GithubRepository
import parinexus.sample.githubevents.data.repository.model.RepoActor
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoGithubRepository

class GithubEventEntityMapperTest {

    private val robot = Robot()

    @Test
    fun `Actor_toRepo maps all fields`() = RUN_UNIT_TEST(robot) {
        GIVEN { givenActor(id = 42L, login = "alice", avatar = "https://avatars.githubusercontent.com/u/42?v=4") }
        WHEN  { mapActor() }
        THEN  { assertActorMapped() }
    }

    @Test
    fun `GithubRepository_toRepo maps all fields`() = RUN_UNIT_TEST(robot) {
        GIVEN { givenRepo(id = 2001L, name = "owner/repo1", url = "https://api.github.com/repos/owner/repo1") }
        WHEN  { mapRepo() }
        THEN  { assertRepoMapped() }
    }

    @Test
    fun `GitHubEvent_toRepo maps id, actor, repo, createdAt`() = RUN_UNIT_TEST(robot) {
        GIVEN { givenActor(id = 7L, login = "bob", avatar = "https://avatars.githubusercontent.com/u/7?v=4") }
        AND   { givenRepo(id = 3001L, name = "owner/repoX", url = "https://api.github.com/repos/owner/repoX") }
        AND   { givenEvent(id = "evt-123", type = EventType.PushEvent, createdAt = "2024-01-01T00:00:00Z") }
        WHEN  { mapEvent() }
        THEN  { assertEventMappedWithRepo() }
    }

    private class Robot : BaseRobot() {
        private var actor: Actor? = null
        private var repo: GithubRepository? = null
        private var event: GitHubEvent? = null

        private var mappedActor: RepoActor? = null
        private var mappedRepo: RepoGithubRepository? = null
        private var mappedEvent: RepoEvent? = null

        fun givenActor(id: Long, login: String, avatar: String) {
            actor = Actor(id = id, login = login, avatarUrl = avatar)
        }

        fun givenRepo(id: Long, name: String, url: String) {
            repo = GithubRepository(id = id, name = name, url = url)
        }

        fun givenEvent(id: String, type: EventType, createdAt: String) {
            val actor = requireNotNull(actor) { "Call givenActor() before givenEvent()" }
            val repo = requireNotNull(repo) { "Call givenRepo() before givenEvent()" }
            event = GitHubEvent(
                id = id,
                type = type,
                actor = actor,
                githubRepository = repo,
                createdAt = createdAt
            )
        }

        fun mapActor() { mappedActor = requireNotNull(actor).toRepo() }
        fun mapRepo() { mappedRepo = requireNotNull(repo).toRepo() }
        fun mapEvent() { mappedEvent = requireNotNull(event).toRepo() }

        fun assertActorMapped() {
            val a = requireNotNull(actor); val m = requireNotNull(mappedActor)
            assertEquals(a.id, m.id)
            assertEquals(a.login, m.login)
            assertEquals(a.avatarUrl, m.avatarUrl)
        }

        fun assertRepoMapped() {
            val r = requireNotNull(repo); val m = requireNotNull(mappedRepo)
            assertEquals(r.id, m.id)
            assertEquals(r.name, m.name)
            assertEquals(r.url, m.url)
        }

        fun assertEventMappedWithRepo() {
            val e = requireNotNull(event); val m = requireNotNull(mappedEvent)
            assertEquals(e.id, m.id)

            assertEquals(e.actor.id, m.actor.id)
            assertEquals(e.actor.login, m.actor.login)
            assertEquals(e.actor.avatarUrl, m.actor.avatarUrl)

            val mr = requireNotNull(m.repo)
            assertEquals(e.githubRepository.id, mr.id)
            assertEquals(e.githubRepository.name, mr.name)
            assertEquals(e.githubRepository.url, mr.url)

            assertEquals(e.createdAt, m.createdAt)
        }
    }
}