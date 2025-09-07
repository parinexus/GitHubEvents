package parinexus.sample.githubevents.features.eventdetail.presentation

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren

class EventDetailScreenRobot(
    private val rule: ComposeTestRule
) {
    var backClicked = false
        private set
    var retryClicked = false
        private set

    fun markBack() { backClicked = true }
    fun markRetry() { retryClicked = true }

    fun assertLoadingVisible() = apply {
        val nodes = rule.onAllNodes(hasTestTag("eventDetailLoading")).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) rule.onNodeWithTag("eventDetailLoading").assertIsDisplayed()
    }

    fun assertErrorVisible() = apply {
        val nodes = rule.onAllNodes(hasTestTag("eventDetailError")).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) rule.onNodeWithTag("eventDetailError").assertIsDisplayed()
        else rule.onNodeWithText("Retry", substring = true, ignoreCase = true).assertIsDisplayed()
    }

    fun assertResultVisible() = apply {
        val nodes = rule.onAllNodes(hasTestTag("eventDetailResult")).fetchSemanticsNodes()
        if (nodes.isNotEmpty()) rule.onNodeWithTag("eventDetailResult").assertIsDisplayed()
    }

    // ---------- interactions
    fun clickBack() = apply {
        val byTag = rule.onAllNodes(hasTestTag("eventDetailBack")).fetchSemanticsNodes()
        if (byTag.isNotEmpty()) rule.onNodeWithTag("eventDetailBack").performClick()
        else rule.onNodeWithContentDescription("back", substring = true, ignoreCase = true).performClick()
    }

    fun clickRetry() = apply {
        val errorTagged = rule.onAllNodes(hasTestTag("eventDetailError")).fetchSemanticsNodes()
        if (errorTagged.isNotEmpty()) {
            rule.onNode(hasTestTag("eventDetailError"))
                .onChildren()
                .filterToOne(hasClickAction())
                .performClick()
        } else {
            rule.onNodeWithText("Retry", substring = true, ignoreCase = true).performClick()
        }
    }

    fun assertBackInvoked() = apply {
        check(backClicked) { "Expected navigateBack to be invoked, but it wasn’t." }
    }
    fun assertRetryInvoked() = apply {
        check(retryClicked) { "Expected retry to be invoked, but it wasn’t." }
    }
}
