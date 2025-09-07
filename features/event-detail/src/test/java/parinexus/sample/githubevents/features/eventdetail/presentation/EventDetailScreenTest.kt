package parinexus.sample.githubevents.features.eventdetail.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.features.eventdetail.presentation.ui.EventDetailScaffold

@RunWith(org.robolectric.RobolectricTestRunner::class)
@Config(sdk = [android.os.Build.VERSION_CODES.TIRAMISU])
class EventDetailScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val robot by lazy { EventDetailScreenRobot(composeRule) }

    @Before
    fun setup() {
        org.robolectric.shadows.ShadowBuild.setFingerprint("robolectric")
        org.robolectric.shadows.ShadowBuild.setBrand("google")
        org.robolectric.shadows.ShadowBuild.setDevice("Pixel5")
    }

    @Test
    fun shows_loading_state() {
        composeRule.setContent {
            MaterialTheme {
                EventDetailScaffold(
                    viewState = EventDetailScreenViewState.Loading,
                    actions = EventDetailScreenActions(
                        retry = { robot.markRetry() },
                        navigateBack = { robot.markBack() }
                    )
                )
            }
        }

        robot.assertLoadingVisible()
    }

    @Test
    fun shows_error_and_retry_calls_callback() {
        composeRule.setContent {
            MaterialTheme {
                EventDetailScaffold(
                    viewState = EventDetailScreenViewState.Error,
                    actions = EventDetailScreenActions(
                        retry = { robot.markRetry() },
                        navigateBack = { robot.markBack() }
                    )
                )
            }
        }

        robot.assertErrorVisible()
            .clickRetry()
            .assertRetryInvoked()
    }

    @Test
    fun clicking_back_triggers_navigation() {
        composeRule.setContent {
            MaterialTheme {
                EventDetailScaffold(
                    viewState = EventDetailScreenViewState.Loading,
                    actions = EventDetailScreenActions(
                        retry = { robot.markRetry() },
                        navigateBack = { robot.markBack() }
                    )
                )
            }
        }

        robot.clickBack()
            .assertBackInvoked()
    }
}
