package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.awaitCancellation
import parinexus.sample.githubevents.features.search.presentation.SearchScreenActions
import parinexus.sample.githubevents.features.search.presentation.SearchScreenViewModel

@Composable
internal fun SearchScreenLoader(
    searchScreenViewModel: SearchScreenViewModel,
    navigateToEventDetail: (eventId: String) -> Unit,
    screenLifecycle: Lifecycle

) {
    val viewState =
        searchScreenViewModel.events.collectAsLazyPagingItems()

    LaunchedEffect(screenLifecycle) {
        screenLifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            searchScreenViewModel.onScreenResumed()
            try {
                awaitCancellation()
            } finally {
                searchScreenViewModel.onScreenStopped()
            }
        }
    }

    val actions = SearchScreenActions(
        openEventDetail = navigateToEventDetail,
        onStartSearch = { }
    )

    SearchScreenScaffold(items = viewState, actions = actions)
}