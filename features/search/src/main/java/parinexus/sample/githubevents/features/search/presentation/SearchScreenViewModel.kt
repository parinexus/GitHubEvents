package parinexus.sample.githubevents.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import javax.inject.Inject
import parinexus.sample.githubevents.features.searchapi.usecase.SearchEventsUseCase
import parinexus.sample.githubevents.features.searchapi.usecase.StartEventsPollingUseCase
import parinexus.sample.githubevents.features.searchapi.usecase.StopEventsPollingUseCase

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchEventsUseCase,
    private val startPolling: StartEventsPollingUseCase,
    private val stopPolling: StopEventsPollingUseCase
) : ViewModel() {

    val events: Flow<PagingData<UserEvent>> = searchUseCase().cachedIn(viewModelScope)

    fun onScreenResumed() = startPolling(viewModelScope, 10_000L, 30, startImmediately = false)
    fun onScreenStopped() = stopPolling()

    override fun onCleared() {
        stopPolling()
        super.onCleared()
    }
}