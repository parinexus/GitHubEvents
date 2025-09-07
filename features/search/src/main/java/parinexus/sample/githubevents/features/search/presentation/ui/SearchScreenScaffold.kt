package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import parinexus.sample.githubevents.features.search.presentation.SearchScreenActions
import parinexus.sample.githubevents.features.searchapi.model.UserEvent

@Composable
internal fun SearchScreenScaffold(
    items: LazyPagingItems<UserEvent>,
    actions: SearchScreenActions
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { SimpleTopBar() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        MainContent(
            modifier = Modifier.padding(innerPadding),
            lazyPagingItems = items,
            actions = actions,
        )
        SnackbarHost(items)
    }
}