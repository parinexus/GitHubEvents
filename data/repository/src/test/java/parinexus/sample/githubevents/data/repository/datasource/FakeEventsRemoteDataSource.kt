package parinexus.sample.githubevents.data.repository.datasource

import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.port.RemoteFetchResult

class FakeEventsRemoteDataSource : EventsRemoteDataSource {
    var next: RemoteFetchResult<RepoEvent> = RemoteFetchResult(200, emptyList(), null)
    override suspend fun getEvents(limit: Int, page: Int): RemoteFetchResult<RepoEvent> = next
}