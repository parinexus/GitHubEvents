package parinexus.sample.githubevents.features.eventdetail.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [android.os.Build.VERSION_CODES.TIRAMISU])
class EventDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val robot by lazy { EventDetailScreenRobot(composeRule) }

    private val viewModel: EventDetailScreenViewModel = mockk(relaxed = true)
    private lateinit var uiStateFlow: MutableStateFlow<EventDetailScreenViewState>

    @Before
    fun setup() {
        org.robolectric.shadows.ShadowBuild.setFingerprint("robolectric")
        org.robolectric.shadows.ShadowBuild.setBrand("google")
        org.robolectric.shadows.ShadowBuild.setDevice("Pixel5")

        uiStateFlow = MutableStateFlow<EventDetailScreenViewState>(EventDetailScreenViewState.Loading)
        every { viewModel.uiState } returns uiStateFlow
    }

    @Test
    fun shows_loading_state() {
        composeRule.setContent {
            MaterialTheme {
                EventDetailScreen(
                    eventDetailScreenViewModel = viewModel,
                    openLink = {},
                    navigateBack = {}
                )
            }
        }
        robot.assertLoadingVisible()
    }

    @Test
    fun shows_error_and_retry_calls_callback() {
        uiStateFlow.value = EventDetailScreenViewState.Error

        composeRule.setContent {
            MaterialTheme {
                EventDetailScreen(
                    eventDetailScreenViewModel = viewModel,
                    openLink = {},
                    navigateBack = {}
                )
            }
        }

        robot.assertErrorVisible()
            .clickRetry()

        verify(exactly = 1) { viewModel.retry() }
    }

    @Test
    fun clicking_back_triggers_navigation() {
        var backInvoked = false
        composeRule.setContent {
            MaterialTheme {
                EventDetailScreen(
                    eventDetailScreenViewModel = viewModel,
                    openLink = {},
                    navigateBack = { backInvoked = true }
                )
            }
        }

        robot.clickBack()
            .assertBackInvoked(backInvoked)
    }
}