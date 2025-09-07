package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import parinexus.sample.githubevents.feature.search.R
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.libraries.design.theme.Red

@Composable
internal fun SnackbarHost(lazyPagingItems: LazyPagingItems<UserEvent>?) {
    val state = remember { SnackbarHostState() }
    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = state,
            snackbar = { snackbarData ->
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .border(1.dp, MaterialTheme.colors.error, RoundedCornerShape(10.dp)),
                    action = {
                        TextButton(
                            onClick = {
                                lazyPagingItems?.retry()
                                state.currentSnackbarData?.dismiss()
                            }
                        ) { Text(text = snackbarData.actionLabel ?: "", color = Red) }
                    },
                    shape = RoundedCornerShape(10.dp),
                    elevation = 0.dp,
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onError,
                ) { Text(text = snackbarData.message) }
            }
        )
    }
    val pagingAppendLoadState = lazyPagingItems?.loadState?.append
    if (pagingAppendLoadState is LoadState.Error) {
        val errorText = stringResource(R.string.snackbarErrorText)
        val retryText = stringResource(R.string.retry)
        LaunchedEffect(pagingAppendLoadState.error) {
            state.showSnackbar(
                message = errorText,
                actionLabel = retryText,
                duration = SnackbarDuration.Indefinite
            )
        }
    }
}
