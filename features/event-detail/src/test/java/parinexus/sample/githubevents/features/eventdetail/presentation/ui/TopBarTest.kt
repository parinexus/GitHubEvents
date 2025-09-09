package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreen
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenViewModel
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenViewState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [android.os.Build.VERSION_CODES.TIRAMISU], instrumentedPackages = ["androidx.loader.content"])
class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topBar_shows_icon_and_backButton_and_back_works() {
        val vm: EventDetailScreenViewModel = mockk(relaxed = true)
        every { vm.uiState } returns MutableStateFlow<EventDetailScreenViewState>(EventDetailScreenViewState.Loading)

        var backClicked = false

        composeTestRule.setContent {
            MaterialTheme {
                EventDetailScreen(
                    eventDetailScreenViewModel = vm,
                    openLink = {},
                    navigateBack = { backClicked = true }
                )
            }
        }

        // icon & back button are visible
        composeTestRule.onNodeWithContentDescription("gitHubIcon").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("backButton").assertIsDisplayed()

        // click back
        composeTestRule.onNodeWithContentDescription("backButton").performClick()
        assert(backClicked) { "Expected navigateBack to be invoked, but it wasn't." }
    }
}