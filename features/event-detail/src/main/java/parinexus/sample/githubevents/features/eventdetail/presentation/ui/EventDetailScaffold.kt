package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenActions
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenViewState
import parinexus.sample.githubevents.libraries.design.ui.ErrorState

@Composable
internal fun EventDetailScaffold(
    viewState: EventDetailScreenViewState,
    actions: EventDetailScreenActions
) {
    Scaffold(
        topBar = { TopBar(onBackClicked = actions.navigateBack) },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(vertical = 36.dp)
            ) {
                when (viewState) {
                    is EventDetailScreenViewState.Error -> ErrorState(
                        modifier = Modifier.align(Alignment.Center),
                        onRetry = actions.retry
                    )
                    is EventDetailScreenViewState.Loading -> LoadingState()
                    is EventDetailScreenViewState.Result -> ResultState(
                        eventDetail = viewState.userEvent,
                    )
                }
            }
        }
    )
}
