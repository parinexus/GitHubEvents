package parinexus.sample.githubevents.features.eventdetail.presentation

import parinexus.sample.githubevents.features.eventdetail.exception.MissingEventIdException
import parinexus.sample.githubevents.features.eventdetailapi.presentation.EventDetailViewModel
import parinexus.sample.githubevents.features.eventdetailapi.usecase.GetEventDetailUseCase
import parinexus.sample.githubevents.libraries.navigation.DestinationArgs
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEventDetailUseCase: GetEventDetailUseCase
) : EventDetailViewModel() {

    override val eventId: String = savedStateHandle.get<String>(DestinationArgs.EVENT_ID)
        ?: throw MissingEventIdException

    private val viewModelState = MutableStateFlow<EventDetailScreenViewState>(
        EventDetailScreenViewState.Loading
    )

    val uiState = viewModelState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = viewModelState.value
    )

    init {
        getEventDetail()
    }

    override fun getEventDetail() {
        viewModelScope.launch {
            viewModelState.emit(EventDetailScreenViewState.Loading)

            runCatching { getEventDetailUseCase(eventId) }
                .onSuccess { result ->

                    val state = when (result) {
                        null -> EventDetailScreenViewState.Error
                        else -> EventDetailScreenViewState.Result(result)
                    }
                    viewModelState.emit(state)
                }
                .onFailure { t ->
                    viewModelState.emit(EventDetailScreenViewState.Error)
                }
        }

    }

    fun retry() {
        getEventDetail()
    }
}
