// data/repository/GitHubRepositoryImpl.kt
package parinexus.sample.githubevents.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import parinexus.sample.githubevents.data.repository.datasource.GitHubDataSource
import parinexus.sample.githubevents.data.repository.mapper.toDomainEvent
import parinexus.sample.githubevents.data.repository.store.EventStore
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val gitHubDataSource: GitHubDataSource,
    private val eventStore: EventStore,
) : GitHubRepository {

    override fun getEvents(): Flow<PagingData<Event>> =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                initialLoadSize = 30,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { gitHubDataSource.getEvents() }
        ).flow
            .map { paging -> paging.map { it.toDomainEvent() } }
            .map { paging -> paging.map { event -> eventStore.put(event); event } }

    override fun getEventById(id: String): Event? = eventStore.get(id)
}