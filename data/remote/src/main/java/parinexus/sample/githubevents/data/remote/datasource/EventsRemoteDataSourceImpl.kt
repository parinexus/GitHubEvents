package parinexus.sample.githubevents.data.remote.datasource

import parinexus.sample.githubevents.data.remote.api.Apis
import parinexus.sample.githubevents.data.remote.entity.EventType
import parinexus.sample.githubevents.data.remote.mapper.toRepo
import parinexus.sample.githubevents.data.repository.datasource.EventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.port.RemoteFetchResult
import javax.inject.Inject

class EventsRemoteDataSourceImpl @Inject constructor(
    private val apis: Apis
) : EventsRemoteDataSource {

    override suspend fun getEvents(limit: Int, page: Int): RemoteFetchResult<RepoEvent> {
        val resp = apis.getEvents(limit = limit, page = page)
        val code = resp.code()
        val link = resp.headers()["Link"]
        val items = if (code in 200..299) {
            resp.body().orEmpty()
                .filter { it.type in EventType.subscribedTypes }
                .map { it.toRepo() }
        } else emptyList()
        return RemoteFetchResult(code = code, items = items, linkHeader = link)
    }
}
