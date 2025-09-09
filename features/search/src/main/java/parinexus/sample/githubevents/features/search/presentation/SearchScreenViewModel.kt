package parinexus.sample.githubevents.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import javax.inject.Inject
import parinexus.sample.githubevents.features.searchapi.usecase.SearchEventsUseCase
import parinexus.sample.githubevents.features.searchapi.usecase.StartEventsPollingUseCase
import parinexus.sample.githubevents.features.searchapi.usecase.StopEventsPollingUseCase

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchUseCase: SearchEventsUseCase,
    private val startPolling: StartEventsPollingUseCase,
    private val stopPolling: StopEventsPollingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenViewState())
    val uiState: StateFlow<SearchScreenViewState> = _uiState.asStateFlow()

    private val eventsFlow: Flow<PagingData<UserEvent>> = searchUseCase().cachedIn(viewModelScope)

    init {
        onStartSearch()
    }

    fun onStartSearch() {
        _uiState.update { it.copy(events = eventsFlow) }
    }

    fun onScreenResumed() = startPolling(viewModelScope, startImmediately = false)
    fun onScreenStopped() = stopPolling()

    override fun onCleared() {
        stopPolling()
        super.onCleared()
    }
}