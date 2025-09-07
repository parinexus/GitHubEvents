package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import parinexus.sample.githubevents.libraries.design.theme.GitHubEventsTheme
import parinexus.sample.githubevents.libraries.test.BaseRobot
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UI_TEST
import androidx.compose.material.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(instrumentedPackages = ["androidx.loader.content"])
class TopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val robot = Robot(composeTestRule)

    @Test
    fun test_viewsAreDisplayed() = RUN_UI_TEST(robot) {
        GIVEN { createTopBar() }
        THEN { checkTopBarIconIsDisplayed() }
        AND { checkBackButtonIsDisplayed() }
    }

    @Test
    fun test_backButtonWorks() = RUN_UI_TEST(robot) {
        GIVEN { createTopBar() }
        AND { mockBackButtonAction() }
        WHEN { checkClickOnBackButton() }
        THEN { checkBackButtonWorks() }
    }

    private class Robot(private val composeContentTestRule: ComposeContentTestRule) : BaseRobot() {

        private val onBackClicked: () -> Unit = mockk()

        fun createTopBar() {
            composeContentTestRule.setContent {
                GitHubEventsTheme {
                    Surface {
                        TopBar(onBackClicked = onBackClicked)
                    }
                }
            }
        }

        fun checkTopBarIconIsDisplayed() {
            composeContentTestRule.onNodeWithContentDescription("gitHubIcon").assertIsDisplayed()
        }

        fun checkBackButtonIsDisplayed() {
            composeContentTestRule.onNodeWithContentDescription("backButton").assertIsDisplayed()
        }

        fun checkClickOnBackButton() {
            composeContentTestRule.onNodeWithContentDescription("backButton").performClick()
        }

        fun checkBackButtonWorks() {
            verify { onBackClicked.invoke() }
        }

        fun mockBackButtonAction() {
            every { onBackClicked.invoke() } answers { }
        }
    }
}