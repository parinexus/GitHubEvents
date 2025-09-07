package parinexus.sample.githubevents.data.local.datasource

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.local.robots.RemoteKeysLocalDataSourceImplRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class RemoteKeysLocalDataSourceImplTest {

    private lateinit var robot: RemoteKeysLocalDataSourceImplRobot

    @Before
    fun setup() { robot = RemoteKeysLocalDataSourceImplRobot() }

    @After
    fun tearDown() { robot.close() }

    @Test
    fun insert_and_getById_returns_values() = RUN_UNIT_TEST(robot) {
        GIVEN { insert(rk("e1", prev = 1, next = 2)) }
        WHEN  { loadById("e1") }
        THEN  { assertLoaded("e1", prev = 1, next = 2) }
        AND   { assertNullById("missing") }
    }

    @Test
    fun insertAll_replace_on_conflict_by_primaryKey() = RUN_UNIT_TEST(robot) {
        GIVEN {
            insert(rk("e1", prev = 1, next = 2))
        }
        WHEN  {
            insert(rk("e1", prev = 7, next = 8))
            loadById("e1")
        }
        THEN  { assertLoaded("e1", prev = 7, next = 8) }
    }

    @Test
    fun clear_removes_all_rows() = RUN_UNIT_TEST(robot) {
        GIVEN { insert(rk("a", 1,2), rk("b", 2,3), rk("c", null, 4)) }
        WHEN  { clear() }
        THEN  { assertNullById("a") }
        AND   { assertNullById("b"); assertNullById("c") }
    }

    @Test
    fun insertAll_accepts_empty_list_noop() = RUN_UNIT_TEST(robot) {
        GIVEN { insert(rk("x", prev = 10, next = 11)) }
        WHEN  { insert(*emptyArray()); loadById("x") }
        THEN  { assertLoaded("x", prev = 10, next = 11) }
    }
}
