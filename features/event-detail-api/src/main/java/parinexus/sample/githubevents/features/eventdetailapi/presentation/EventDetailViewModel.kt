package parinexus.sample.githubevents.features.eventdetailapi.presentation

import androidx.lifecycle.ViewModel

abstract class EventDetailViewModel : ViewModel() {

    abstract val eventId: String

    abstract fun getEventDetail()
}
