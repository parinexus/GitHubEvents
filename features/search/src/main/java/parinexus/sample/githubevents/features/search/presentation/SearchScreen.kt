package parinexus.sample.githubevents.features.search.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import parinexus.sample.githubevents.features.search.presentation.ui.SearchScreenLoader

@Composable
fun SearchScreen(
    searchScreenViewModel: SearchScreenViewModel,
    navigateToEventDetail: (eventId: String) -> Unit,
    screenLifecycle: Lifecycle

) {
    SearchScreenLoader(
        searchScreenViewModel = searchScreenViewModel,
        navigateToEventDetail = navigateToEventDetail,
        screenLifecycle=screenLifecycle
    )
}









