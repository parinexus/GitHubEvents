package parinexus.sample.githubevents.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.mapper.toDomainEvent
import parinexus.sample.githubevents.data.repository.util.DEFAULT_PAGE_SIZE
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val eventsLocalDataSource: EventsLocalDataSource,
) : GitHubRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getEvents(): Flow<PagingData<Event>> =
        Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE,
                initialLoadSize = DEFAULT_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { eventsLocalDataSource.pagingSource() }
        ).flow.map { paging -> paging.map { it.toDomainEvent() } }

    override suspend fun getEventById(id: String): Event? =
        eventsLocalDataSource.getById(id)?.toDomainEvent()

}