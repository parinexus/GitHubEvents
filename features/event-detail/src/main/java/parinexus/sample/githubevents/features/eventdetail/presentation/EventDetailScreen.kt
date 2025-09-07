package parinexus.sample.githubevents.features.eventdetail.presentation

import androidx.compose.runtime.Composable
import parinexus.sample.githubevents.features.eventdetail.presentation.ui.EventDetailScreenLoader

@Composable
fun EventDetailScreen(
    eventDetailScreenViewModel: EventDetailScreenViewModel,
    openLink: (link: String) -> Unit = {},
    navigateBack: () -> Unit
) {
    EventDetailScreenLoader(
        eventDetailScreenViewModel = eventDetailScreenViewModel,
        openLink = openLink,
        navigateBack = navigateBack
    )
}

