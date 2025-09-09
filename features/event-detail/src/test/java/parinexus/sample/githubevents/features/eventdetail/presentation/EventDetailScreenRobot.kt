package parinexus.sample.githubevents.features.eventdetail.presentation

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule

class EventDetailScreenRobot(
    private val rule: ComposeTestRule
) {
    fun assertLoadingVisible() = apply {
        // LoadingState has testTag("loadingState")
        rule.onNodeWithTag("loadingState").assertIsDisplayed()
    }

    fun assertErrorVisible() = apply {
        // ErrorState از دیزاین‌لایب تگ اختصاصی ندارد؛ دکمه Retry را پیدا می‌کنیم
        rule.onNodeWithText("Retry", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    fun assertResultVisible() = apply {
        // ResultState has testTag("resultState")
        rule.onNodeWithTag("resultState").assertIsDisplayed()
    }

    fun clickBack() = apply {
        rule.onNodeWithContentDescription("backButton").performClick()
    }

    fun clickRetry() = apply {
        // گاهی Retry داخل هیرارکی ErrorState است؛ ساده‌ترین راه همین جست‌وجوی متن است
        rule.onNodeWithText("Retry", substring = true, ignoreCase = true).performClick()
    }

    fun assertBackInvoked(invoked: Boolean) = apply {
        check(invoked) { "Expected navigateBack to be invoked, but it wasn’t." }
    }
}