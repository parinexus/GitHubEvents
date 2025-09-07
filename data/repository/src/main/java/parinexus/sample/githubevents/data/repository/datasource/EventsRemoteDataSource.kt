package parinexus.sample.githubevents.data.repository.datasource

import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.port.RemoteFetchResult

interface EventsRemoteDataSource {
    suspend fun getEvents(limit: Int, page: Int): RemoteFetchResult<RepoEvent>
}
