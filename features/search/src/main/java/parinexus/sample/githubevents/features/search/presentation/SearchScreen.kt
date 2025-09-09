package parinexus.sample.githubevents.features.search.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.awaitCancellation
import parinexus.sample.githubevents.features.search.presentation.ui.EventItem
import parinexus.sample.githubevents.features.search.presentation.ui.NoEventsFoundState
import parinexus.sample.githubevents.libraries.design.theme.GitHubEventsTheme
import parinexus.sample.githubevents.libraries.design.theme.PlaceHolderColor
import parinexus.sample.githubevents.libraries.design.ui.ErrorState

@Composable
fun SearchScreen(
    searchScreenViewModel: SearchScreenViewModel,
    navigateToEventDetail: (eventId: String) -> Unit,
    screenLifecycle: Lifecycle
) {
    val state by searchScreenViewModel.uiState.collectAsState()
    val lazyPagingItems = state.events.collectAsLazyPagingItems()

    LaunchedEffect(screenLifecycle) {
        screenLifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            searchScreenViewModel.onScreenResumed()
            try { awaitCancellation() } finally { searchScreenViewModel.onScreenStopped() }
        }
    }

    val actions = SearchScreenActions(
        openEventDetail = navigateToEventDetail,
        onStartSearch = { }
    )
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "GitHub Events") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        val refreshLoadState = lazyPagingItems.loadState.refresh


        when {
            refreshLoadState is LoadState.Loading -> {
                EventListLoadingState()
            }

            refreshLoadState is LoadState.Error -> {
                ErrorState(onRetry = { lazyPagingItems.retry() })
            }

            lazyPagingItems.loadState.append.endOfPaginationReached &&
                    lazyPagingItems.itemCount == 0 -> {
                NoEventsFoundState()
            }

            else -> {
                val lazyListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    LazyColumn(
                        modifier = Modifier.testTag("eventsList"),
                        state = lazyListState,
                        contentPadding = PaddingValues(bottom = 8.dp),
                    ) {
                        items(
                            count = lazyPagingItems.itemCount,
                            key = { index -> lazyPagingItems[index]?.id ?: index }
                        ) { index ->
                            val item = lazyPagingItems[index]
                            if (item == null) {
                                EventPlaceHolder()
                            } else {
                                EventItem(
                                    item,
                                    onClick = { actions.openEventDetail(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun EventListLoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("eventsListPlaceHolder")
    ) { repeat(20) { EventPlaceHolder() } }
}

@Composable
internal fun EventPlaceHolder() {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(48.dp)
                .background(color = PlaceHolderColor, shape = CircleShape)
        )
        Spacer(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .height(16.dp)
                .background(color = PlaceHolderColor, shape = RoundedCornerShape(8.dp))
        )
    }
}

@Preview(widthDp = 300, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EventPalaceHolderPreview() {
    GitHubEventsTheme {
        Surface {
            EventPlaceHolder()
        }
    }
}







