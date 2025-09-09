package parinexus.sample.githubevents.features.search.presentation

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.paging.PagingData
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.libraries.test.BaseRobot

/**
 * Robot for SearchScreen Compose UI tests.
 *
 * Notes:
 * - We pass a controllable TestLifecycleOwner to SearchScreen.
 * - We provide a MockK SearchScreenViewModel and stub 'events'.
 * - We verify lifecycle forwarding via VM's onScreenResumed/Stopped.
 *
 * Make sure your test dependencies include:
 *   implementation("androidx.lifecycle:lifecycle-runtime-testing:<version>")
 *   testImplementation("io.mockk:mockk:<version>")
 *   testImplementation("org.robolectric:robolectric:<version>")
 *   androidTest/ testImplementation for compose-ui-test as applicable.
 */
class SearchScreenRobot(
    private val composeRule: ComposeContentTestRule
) : BaseRobot(){
    private val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.CREATED)

    private val viewModel: SearchScreenViewModel = mockk(relaxed = true)

    private var lastNavigatedId: String? = null
    private val navigateSpy: (String) -> Unit = { id -> lastNavigatedId = id }

    fun mockViewModelWithEvents(count: Int) {
        val items: List<UserEvent> = List(count) { mockk(relaxed = true) }
        val eventsFlow: Flow<PagingData<UserEvent>> =
            if (count == 0) flowOf(PagingData.empty())
            else flowOf(PagingData.from(items))

        val stateFlow = MutableStateFlow(
            SearchScreenViewState(events = eventsFlow)
        )

        every { viewModel.uiState } returns stateFlow
    }

    fun setContent() {
        composeRule.setContent {
            SearchScreen(
                searchScreenViewModel = viewModel,
                navigateToEventDetail = navigateSpy,
                screenLifecycle = lifecycleOwner.lifecycle
            )
        }
    }

    fun moveLifecycleToResumed() {
        lifecycleOwner.currentState = Lifecycle.State.RESUMED
    }

    fun moveLifecycleToStopped() {
        lifecycleOwner.currentState = Lifecycle.State.DESTROYED
    }

    fun verifyOnScreenResumedCalledExactly(times: Int) {
        verify(exactly = times) { viewModel.onScreenResumed() }
    }

    fun verifyOnScreenStoppedCalledExactly(times: Int) {
        verify(exactly = times) { viewModel.onScreenStopped() }
    }

    fun assertNavigatedTo(expectedId: String) {
        requireNotNull(lastNavigatedId) { "Expected navigation to be triggered, but it wasn't." }
        assert(lastNavigatedId == expectedId) {
            "Expected navigateToEventDetail($expectedId) but was $lastNavigatedId"
        }
    }
}