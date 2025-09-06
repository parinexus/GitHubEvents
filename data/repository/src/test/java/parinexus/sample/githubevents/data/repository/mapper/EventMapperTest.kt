package parinexus.sample.githubevents.data.repository.mapper

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import parinexus.sample.githubevents.data.repository.model.RepoActor
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoGithubRepository
import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.GithubRepository

class RepoMappersTest {

    @Test
    fun `Given RepoActor When toRepo Then copies fields`() {
        val repoActor = RepoActor(
            id = 123L,
            login = "octocat",
            avatarUrl = "https://avatars.githubusercontent.com/u/583231"
        )

        val domain: Actor = repoActor.toRepo()

        assertEquals(123L, domain.id)
        assertEquals("octocat", domain.login)
        assertEquals("https://avatars.githubusercontent.com/u/583231", domain.avatarUrl)
    }

    @Test
    fun `Given RepoGithubRepository When toRepo Then copies fields`() {
        val repoRepo = RepoGithubRepository(
            id = 42L,
            name = "hello-world",
            url = "https://github.com/octocat/hello-world"
        )

        val domain: GithubRepository = repoRepo.toRepo()

        assertEquals(42L, domain.id)
        assertEquals("hello-world", domain.name)
        assertEquals("https://github.com/octocat/hello-world", domain.url)
    }

    @Test
    fun `Given RepoEvent with repo When toDomainEvent Then nested mappings are applied`() {
        val repoEvent = RepoEvent(
            id = "999",
            actor = RepoActor(
                id = 1L,
                login = "alice",
                avatarUrl = "https://img/alice.png"
            ),
            repo = RepoGithubRepository(
                id = 2L,
                name = "sample",
                url = "https://github.com/acme/sample"
            ),
            createdAt = "2024-01-01T00:00:00Z"
        )

        val domain: Event = repoEvent.toDomainEvent()

        assertEquals("999", domain.id)
        assertEquals(1L, domain.actor.id)
        assertEquals("alice", domain.actor.login)
        assertEquals("https://img/alice.png", domain.actor.avatarUrl)
        requireNotNull(domain.repo)
        assertEquals(2L, domain.repo!!.id)
        assertEquals("sample", domain.repo!!.name)
        assertEquals("https://github.com/acme/sample", domain.repo!!.url)
        assertEquals("2024-01-01T00:00:00Z", domain.createdAt)
    }

    @Test
    fun `Given RepoEvent without repo When toDomainEvent Then repo is null`() {
        val repoEvent = RepoEvent(
            id = "1000",
            actor = RepoActor(
                id = 7L,
                login = "bob",
                avatarUrl = "https://img/bob.png"
            ),
            repo = null,
            createdAt = "2024-02-02T02:02:02Z"
        )

        val domain: Event = repoEvent.toDomainEvent()

        assertEquals("1000", domain.id)
        assertEquals(7L, domain.actor.id)
        assertNull(domain.repo)
        assertEquals("2024-02-02T02:02:02Z", domain.createdAt)
    }
}