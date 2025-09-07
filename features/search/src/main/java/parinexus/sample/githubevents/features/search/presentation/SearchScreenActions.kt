package parinexus.sample.githubevents.features.search.presentation

data class SearchScreenActions(
    val openEventDetail: (username: String) -> Unit = {},
    val onStartSearch: () -> Unit = {},
)
