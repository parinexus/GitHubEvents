package parinexus.sample.githubevents.features.eventdetail.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import parinexus.sample.githubevents.features.eventdetail.presentation.ui.LoadingState
import parinexus.sample.githubevents.features.eventdetail.presentation.ui.ResultState
import parinexus.sample.githubevents.libraries.design.R
import parinexus.sample.githubevents.libraries.design.ui.ErrorState

@Composable
fun EventDetailScreen(
    eventDetailScreenViewModel: EventDetailScreenViewModel,
    openLink: (link: String) -> Unit = {},
    navigateBack: () -> Unit
) {
    val viewState by eventDetailScreenViewModel.uiState.collectAsState()
    val actions = EventDetailScreenActions(
        navigateBack = navigateBack,
        retry = eventDetailScreenViewModel::retry,
        openLink = openLink
    )


    Scaffold(
        topBar = {
            TopAppBar(elevation = 0.dp, backgroundColor = MaterialTheme.colors.surface) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("topBar")
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(28.dp),
                        painter = painterResource(id = R.drawable.ic_github),
                        tint = MaterialTheme.colors.onSurface,
                        contentDescription = "gitHubIcon"
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = actions.navigateBack
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            tint = MaterialTheme.colors.onSurface,
                            contentDescription = "backButton"
                        )
                    }
                }
            }
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(vertical = 36.dp)
            ) {
                when (val state = viewState) {
                    is EventDetailScreenViewState.Error -> ErrorState(
                        modifier = Modifier.align(Alignment.Center),
                        onRetry = actions.retry
                    )

                    is EventDetailScreenViewState.Loading -> LoadingState()
                    is EventDetailScreenViewState.Result -> ResultState(
                        eventDetail = state.userEvent,
                        openLink = actions.openLink
                    )
                }
            }
        }
    )
}

