package parinexus.sample.githubevents.domain.interactor

import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import javax.inject.Inject

class GetEventDetailInteractor @Inject constructor(
    private val gitHubRepository: GitHubRepository
) {

    operator fun invoke(eventId: String): Event =
        gitHubRepository.getEventById(eventId) ?: throw NoSuchElementException("Event not found")
}
