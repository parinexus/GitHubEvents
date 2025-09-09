package parinexus.sample.githubevents.features.search.presentation

data class SearchScreenActions(
    val openEventDetail: (eventId: String) -> Unit,
    val onStartSearch: () -> Unit,
)
