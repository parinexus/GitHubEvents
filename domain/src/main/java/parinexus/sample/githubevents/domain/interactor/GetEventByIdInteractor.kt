package parinexus.sample.githubevents.domain.interactor

import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import javax.inject.Inject

class GetEventByIdInteractor @Inject constructor(private val repo: GitHubRepository) {
    suspend operator fun invoke(id: String): Event? = repo.getEventById(id)
}