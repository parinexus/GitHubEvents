package parinexus.sample.githubevents.data.repository.sync

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.repository.robots.EventsPollerRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class EventsPollerTest {

    private lateinit var robot: EventsPollerRobot

    @Before
    fun setup() {
        robot = EventsPollerRobot()
    }

    @Test
    fun start_inserts_only_new_after_lastSeen_and_skips_duplicates() = RUN_UNIT_TEST(robot) {
        GIVEN {
            seedLocal(
                repoEvent(id = "e1", createdAt = 1_000L, login = "old")
            )
            seedRemote(
                code = 200,
                items = listOf(
                    repoEvent(id = "e1", createdAt = 1_000L, login = "old-dup"),
                    repoEvent(id = "e2", createdAt = 2_000L, login = "new"),
                )
            )
        }
        WHEN { startPollerAndWaitFirstInsert() }
        THEN { assertLocalIdsByCreatedDesc(listOf("e2", "e1")) }
        AND { assertLocalSize(2); stopAndDispose() }
    }

    @Test
    fun start_does_nothing_on_304() = RUN_UNIT_TEST(robot) {
        GIVEN {
            seedLocal()
            seedRemote(code = 304, items = emptyList())
        }
        WHEN { startPollerAndWaitSilently() }
        THEN { assertLocalSize(0); stopAndDispose() }
    }

    @Test
    fun start_when_local_empty_fetches_immediately_even_if_startImmediately_false() =
        RUN_UNIT_TEST(robot) {
            GIVEN {
                seedRemote(
                    code = 200,
                    items = listOf(repoEvent(id = "e1", createdAt = 1_000L, login = "new")),
                )
            }
            WHEN { startPollerAndWaitFirstInsert(startImmediately = false) }
            THEN { assertLocalIdsByCreatedDesc(listOf("e1")); assertLocalSize(1); stopAndDispose() }
        }

    @Test
    fun stop_prevents_next_cycles_after_first_iteration() = RUN_UNIT_TEST(robot) {
        GIVEN {
            seedRemote(
                code = 200,
                items = listOf(
                    repoEvent(id = "a", createdAt = 1_000L, login = "a"),
                    repoEvent(id = "b", createdAt = 2_000L, login = "b")
                )
            )
        }
        WHEN {
            startPollerAndWaitFirstInsert()
            stopPoller()

            seedRemote(
                code = 200,
                items = listOf(repoEvent(id = "c", createdAt = 3_000L, login = "c"))
            )
            delayReal(60L)
        }
        THEN { assertLocalIdsByCreatedDesc(listOf("b", "a")); assertLocalSize(2); stopAndDispose() }
    }
}

