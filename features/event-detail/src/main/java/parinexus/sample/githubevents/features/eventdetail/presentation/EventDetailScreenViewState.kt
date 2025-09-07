package parinexus.sample.githubevents.features.eventdetail.presentation

import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent

sealed class EventDetailScreenViewState {
    object Loading : EventDetailScreenViewState()
    object Error : EventDetailScreenViewState()
    data class Result(val userEvent: UserEvent) : EventDetailScreenViewState()
}
