package parinexus.sample.githubevents.features.search.presentation

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import parinexus.sample.githubevents.features.searchapi.model.UserEvent

data class SearchScreenViewState(
    val events: Flow<PagingData<UserEvent>> = flowOf(PagingData.empty())
)
