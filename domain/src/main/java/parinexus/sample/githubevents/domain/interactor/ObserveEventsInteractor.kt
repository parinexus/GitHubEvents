package parinexus.sample.githubevents.domain.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import javax.inject.Inject

class ObserveEventsInteractor @Inject constructor(private val gitHubRepository: GitHubRepository) {
    operator fun invoke(): Flow<PagingData<Event>> = gitHubRepository.getEvents()
}