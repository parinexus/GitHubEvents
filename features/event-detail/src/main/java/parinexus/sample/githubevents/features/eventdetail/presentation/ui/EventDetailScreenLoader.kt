package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenActions
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenViewModel

@Composable
internal fun EventDetailScreenLoader(
    eventDetailScreenViewModel: EventDetailScreenViewModel,
    openLink: (link: String) -> Unit,
    navigateBack: () -> Unit
) {
    val state by eventDetailScreenViewModel.uiState.collectAsState()
    val actions = EventDetailScreenActions(
        navigateBack = navigateBack,
        retry = eventDetailScreenViewModel::retry,
        openLink = openLink
    )

    EventDetailScaffold(viewState = state, actions = actions)
}
