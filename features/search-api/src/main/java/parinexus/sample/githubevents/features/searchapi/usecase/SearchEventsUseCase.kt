package parinexus.sample.githubevents.features.searchapi.usecase

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import parinexus.sample.githubevents.domain.interactor.ObserveEventsInteractor
import parinexus.sample.githubevents.features.searchapi.mapper.toView
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import javax.inject.Inject

class SearchEventsUseCase @Inject constructor(
    private val observeEventsInteractor: ObserveEventsInteractor
) {
    operator fun invoke(): Flow<PagingData<UserEvent>> =
        observeEventsInteractor().map { paging -> paging.map { it.toView() } }
}
