package parinexus.sample.githubevents.features.search.presentation

import io.mockk.clearAllMocks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SearchScreenViewModelTest {

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: TestDispatcher
    private lateinit var robot: SearchScreenViewModelRobot

    @Before
    fun setUp() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)
        Dispatchers.setMain(dispatcher)
        clearAllMocks()
        robot = SearchScreenViewModelRobot(scheduler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun test_use_case_called_on_init_and_emits_paging_data() = RUN_UNIT_TEST(robot) {
        GIVEN { mockUseCaseWithEvents(count = 3) }
        WHEN  { createViewModel() }
        THEN  { verifySearchUseCaseCalledExactly(1) }
        AND   { assertUiHasPagingData() }
    }

    @Test
    fun test_onScreenResumed_starts_polling_with_expected_args() = RUN_UNIT_TEST(robot) {
        GIVEN { mockUseCaseWithEvents(count = 1) }
        WHEN  { createViewModel() }
        AND   { startAutoRefresh(immediate = true) }
        THEN  { verifyStartPollingCalledExactly(1) }
        AND   { verifyStartPollingCalledWith(intervalMillis = 10_000L, maxPages = 30) }
    }

    @Test
    fun test_multiple_resumes_forward_multiple_start_calls() = RUN_UNIT_TEST(robot) {
        GIVEN { mockUseCaseWithEvents(count = 1) }
        WHEN  { createViewModel() }
        AND   { startAutoRefresh(immediate = true) }
        AND   { startAutoRefresh(immediate = true) }
        THEN  { verifyStartPollingCalledExactly(2) }
    }

    @Test
    fun test_onScreenStopped_stops_polling() = RUN_UNIT_TEST(robot) {
        GIVEN { mockUseCaseWithEvents(count = 2) }
        WHEN  { createViewModel() }
        AND   { startAutoRefresh(immediate = true) }
        AND   { stopAutoRefreshAndDrain() }
        THEN  { verifyStopPollingCalledExactly(1) }
    }

    @Test
    fun test_onCleared_also_stops_polling() = RUN_UNIT_TEST(robot) {
        GIVEN { mockUseCaseWithEvents(count = 2) }
        WHEN  { createViewModel() }
        AND   { startAutoRefresh(immediate = true) }
        AND   { clearViewModelReflectively() }
        THEN  { verifyStopPollingCalledExactly(1) }
    }
}