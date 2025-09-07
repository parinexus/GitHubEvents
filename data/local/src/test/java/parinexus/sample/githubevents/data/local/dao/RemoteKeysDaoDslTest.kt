package parinexus.sample.githubevents.data.local.dao

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.local.entity.RemoteKeys
import parinexus.sample.githubevents.data.local.robots.RemoteKeysDaoRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class RemoteKeysDaoTest {

    private lateinit var robot: RemoteKeysDaoRobot

    @Before fun setup() { robot = RemoteKeysDaoRobot()
    }
    @After fun tearDown() { robot.close() }

    @Test
    fun query_by_id_returns_values_and_null_for_missing() = RUN_UNIT_TEST(robot) {
        GIVEN {
            insert(
                RemoteKeys(eventId = "e1", prevKey = null, nextKey = 2),
                RemoteKeys(eventId = "e2", prevKey = 1,    nextKey = 3)
            )
        }
        WHEN  { loadById("e1"); loadById("e2") }
        THEN  {
            assertPrevNext("e1", prev = null, next = 2)
            assertPrevNext("e2", prev = 1,    next = 3)
        }
        AND   { assertNullById("missing") }
    }

    @Test
    fun replace_on_same_pk_updates_row() = RUN_UNIT_TEST(robot) {
        GIVEN { insert(RemoteKeys("e1", prevKey = null, nextKey = 2)) }
        WHEN  { insert(RemoteKeys("e1", prevKey = 10, nextKey = 11)) } // REPLACE
        THEN  { assertPrevNext("e1", prev = 10, next = 11) }
        AND   { assertIdExists("e1") }
    }

    @Test
    fun clear_makes_table_empty() = RUN_UNIT_TEST(robot) {
        GIVEN { insert(RemoteKeys("a", 1, 2), RemoteKeys("b", 2, 3)) }
        WHEN  { clear() }
        THEN  { assertNullById("a") }
        AND   { assertNullById("b") }
    }
}