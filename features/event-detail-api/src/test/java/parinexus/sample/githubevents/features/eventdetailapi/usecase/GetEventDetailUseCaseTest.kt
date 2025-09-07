package parinexus.sample.githubevents.features.eventdetailapi.usecase

import io.mockk.*
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
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.features.eventdetailapi.robots.GetEventDetailUseCaseRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN

typealias DomainEvent = Event

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GetEventDetailUseCaseTest {

    private lateinit var scheduler: TestCoroutineScheduler
    private lateinit var dispatcher: TestDispatcher
    private lateinit var robot: GetEventDetailUseCaseRobot

    @Before
    fun setUp() {
        scheduler = TestCoroutineScheduler()
        dispatcher = StandardTestDispatcher(scheduler)
        Dispatchers.setMain(dispatcher)
        clearAllMocks()
        robot = GetEventDetailUseCaseRobot(scheduler)
    }

    @After
    fun tearDown() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun test_invoke_WHEN_event_exists_THEN_maps_and_returns_UserEvent() = RUN_UNIT_TEST(robot) {
        GIVEN { mockInteractorReturnsEventFor(id = "42") }
        AND   { mockMapperToReturnUserEvent() }
        WHEN  { invokeUseCase("42") }
        THEN  { assertResultIsMapped() }
        AND   { verifyInteractorCalledExactly(1) }
        AND   { verifyInteractorCalledWith("42") }
    }

    @Test
    fun test_invoke_WHEN_event_not_found_THEN_returns_null() = RUN_UNIT_TEST(robot) {
        GIVEN { mockInteractorReturnsNullFor(id = "404") }
        WHEN  { invokeUseCase("404") }
        THEN  { assertResultIsNull() }
        AND   { verifyInteractorCalledExactly(1) }
        AND   { verifyInteractorCalledWith("404") }
    }
}
