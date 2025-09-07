package parinexus.sample.githubevents.features.search.presentation

import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.clearAllMocks
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

/**
 * UI tests for SearchScreen that drive a test Lifecycle and
 * verify the ViewModel receives lifecycle-driven calls via the loader.
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SearchScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var robot: SearchScreenRobot

    @Before
    fun setUp() {
        clearAllMocks()
        robot = SearchScreenRobot(composeRule)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_resuming_calls_viewModel_onScreenResumed() = RUN_UNIT_TEST(robot) {
        GIVEN { mockViewModelWithEvents(count = 3) }
        WHEN  { setContent() }
        AND   { moveLifecycleToResumed() }
        THEN  { verifyOnScreenResumedCalledExactly(1) }
    }

    @Test
    fun test_stopping_calls_viewModel_onScreenStopped() = RUN_UNIT_TEST(robot) {
        GIVEN { mockViewModelWithEvents(count = 2) }
        WHEN  { setContent() }
        AND   { moveLifecycleToResumed() }
        AND   { moveLifecycleToStopped() }
        THEN  { verifyOnScreenStoppedCalledExactly(1) }
    }

    @Test
    fun test_multiple_resumes_forward_multiple_calls() = RUN_UNIT_TEST(robot) {
        GIVEN { mockViewModelWithEvents(count = 1) }
        WHEN  { setContent() }
        AND   { moveLifecycleToResumed() }
        AND   { moveLifecycleToStopped() }
        AND   { moveLifecycleToResumed() }
        THEN  { verifyOnScreenResumedCalledExactly(2) }
    }
}
