package parinexus.sample.githubevents.features.eventdetailapi.usecase

import parinexus.sample.githubevents.domain.interactor.GetEventByIdInteractor
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.features.eventdetailapi.mapper.toDetailsView
import javax.inject.Inject

class GetEventDetailUseCase @Inject constructor(
    private val getById: GetEventByIdInteractor
) {

    suspend operator fun invoke(id: String): UserEvent? =
        getById(id)?.toDetailsView()
}
