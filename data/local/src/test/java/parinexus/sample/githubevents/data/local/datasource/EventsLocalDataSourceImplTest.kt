package parinexus.sample.githubevents.data.local.datasource

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.local.robots.EventsLocalDataSourceImplRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class EventsLocalDataSourceImplTest {

    private lateinit var robot: EventsLocalDataSourceImplRobot

    @Before
    fun setup() {
        robot = EventsLocalDataSourceImplRobot()
    }

    @After
    fun tearDown() {
        robot.close()
    }

    @Test
    fun getById_returns_item_and_null_for_missing() = RUN_UNIT_TEST(robot) {
        GIVEN {
            upsert(
                re(id = "e1", actorLogin = "octocat"),
                re(id = "e2", actorLogin = "hubber")
            )
        }
        WHEN  { loadById("e1") }
        THEN  { assertLoadedId("e1"); assertLoadedActor("octocat") }
        AND   { assertNullById("missing") }
    }

    @Test
    fun upsert_ignore_keeps_original_on_duplicate_id() = RUN_UNIT_TEST(robot) {
        GIVEN { upsert(re(id = "dup", actorLogin = "first")) }
        WHEN  { upsert(re(id = "dup", actorLogin = "second")) }
        THEN  { assertActorById("dup", expected = "first") }
        AND   { assertIdExists("dup") }
    }

    @Test
    fun pagingSource_orders_by_createdAt_desc() = RUN_UNIT_TEST(robot) {
        GIVEN {
            upsert(
                re(id = "a", createdAt = 1_000),
                re(id = "b", createdAt = 3_000),
                re(id = "c", createdAt = 2_000),
            )
        }
        WHEN  { loadFirstPage(loadSize = 10) }
        THEN  { assertPageOrder(listOf("b", "c", "a")) }
        AND   { assertPageSize(3) }
    }

    @Test
    fun latestCreatedAt_returns_max() = RUN_UNIT_TEST(robot) {
        GIVEN {
            upsert(
                re(id = "x", createdAt = 11),
                re(id = "y", createdAt = 42),
                re(id = "z", createdAt = 7),
            )
        }
        WHEN  { queryLatestCreatedAt() }
        THEN  { assertLatest(42L) }
        AND   { assertLatestNotNull() }
    }

    @Test
    fun clearAll_empties_table_and_paging() = RUN_UNIT_TEST(robot) {
        GIVEN {
            upsert(re("1"), re("2"))
            assertIdExists("1")
        }
        WHEN  { clearAll() }
        THEN  { assertNullById("1") }
        AND   { assertPagingEmpty() }
    }
}
