package parinexus.sample.githubevents.features.eventdetail.presentation

data class EventDetailScreenActions(
    val navigateBack: () -> Unit = {},
    val retry: () -> Unit = {},
    val openLink: (link: String) -> Unit = {}
)
