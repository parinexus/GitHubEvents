package parinexus.sample.githubevents.data.repository

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.repository.robots.GitHubRepositoryImplRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GitHubRepositoryImplTest {

    private lateinit var robot: GitHubRepositoryImplRobot

    @Before
    fun setup() {
        robot = GitHubRepositoryImplRobot()
    }

    @Test
    fun paging_maps_repo_to_domain_and_emits_items() = RUN_UNIT_TEST(robot) {
        GIVEN {
            seedRemote(
                listOf(
                    repoEvent(id = "e2", createdAt = 2_000L, login = "b"),
                    repoEvent(id = "e1", createdAt = 1_000L, login = "a"),
                )
            )
        }
        WHEN  { loadSnapshot() }
        THEN  { assertPageIds(listOf("e2", "e1")) }
        AND   { assertFirstActor("b") }
    }

    @Test
    fun getEventById_returns_item_and_null_for_missing() = RUN_UNIT_TEST(robot) {
        GIVEN {
            seedRemote(
                listOf(
                    repoEvent(id = "e1", createdAt = 1_000L, login = "octocat"),
                    repoEvent(id = "e2", createdAt = 2_000L, login = "hubber"),
                )
            )
        }
        WHEN  { loadSnapshot() }
        THEN  { assertGetByIdLogin("e1", expectedLogin = "octocat") }
        AND   { assertGetByIdIsNull("missing") }
    }

    @Test
    fun paging_orders_by_createdAt_desc() = RUN_UNIT_TEST(robot) {
        GIVEN {
            seedRemote(
                listOf(
                    repoEvent(id = "a", createdAt = 1_000L, login = "a"),
                    repoEvent(id = "b", createdAt = 3_000L, login = "b"),
                    repoEvent(id = "c", createdAt = 2_000L, login = "c"),
                )
            )
        }
        WHEN  { loadSnapshot() }
        THEN  { assertPageIds(listOf("b", "c", "a")) }
        AND   { assertPageSize(3) }
    }
}