package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import parinexus.sample.githubevents.features.search.presentation.SearchScreenActions
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.libraries.design.ui.ErrorState

@Composable
internal fun MainContent(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<UserEvent>?,
    actions: SearchScreenActions,
) {
    val topInset = 54.dp + 16.dp
    val refreshLoadState = lazyPagingItems?.loadState?.refresh

    when {
        lazyPagingItems == null -> {
            InitialState(modifier = modifier, onStartSearch = actions.onStartSearch)
        }

        refreshLoadState is LoadState.Loading -> {
            EventListLoadingState(modifier = modifier.padding(top = topInset))
        }

        refreshLoadState is LoadState.Error -> {
            ErrorState(modifier = modifier, onRetry = { lazyPagingItems.retry() })
        }


        lazyPagingItems.loadState.append.endOfPaginationReached && lazyPagingItems.itemCount == 0 -> {
            NoEventsFoundState(modifier = modifier)
        }

        else -> {
            val lazyListState = rememberSaveable(saver = LazyListState.Saver) {
                LazyListState()
            }
            EventsList(
                modifier = modifier,
                lazyListState = lazyListState,
                contentPadding = PaddingValues(top = topInset, bottom = 70.dp),
                items = lazyPagingItems,
                actions = actions
            )
        }
    }
}