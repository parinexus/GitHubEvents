package parinexus.sample.githubevents.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.EventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.datasource.RemoteKeysLocalDataSource
import parinexus.sample.githubevents.data.repository.mapper.toDomainEvent
import parinexus.sample.githubevents.data.repository.paging.EventsRemoteMediator
import parinexus.sample.githubevents.data.repository.port.TransactionRunner
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import javax.inject.Inject

class GitHubRepositoryImpl @Inject constructor(
    private val eventsRemoteDataSource: EventsRemoteDataSource,
    private val eventsLocalDataSource: EventsLocalDataSource,
    private val remoteKeysLocalDataSource: RemoteKeysLocalDataSource,
    private val transactionRunner: TransactionRunner,
) : GitHubRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getEvents(): Flow<PagingData<Event>> =
        Pager(
            config = PagingConfig(
                pageSize = 30,
                initialLoadSize = 30,
                enablePlaceholders = false
            ),
            remoteMediator = EventsRemoteMediator(
                remote = eventsRemoteDataSource,
                local = eventsLocalDataSource,
                keys = remoteKeysLocalDataSource,
                tx = transactionRunner,
                pageSize = 30
            ),
            pagingSourceFactory = { eventsLocalDataSource.pagingSource() }
        ).flow
            .map { paging -> paging.map { it.toDomainEvent() } }

    override suspend fun getEventById(id: String): Event? =
        eventsLocalDataSource.getById(id)?.toDomainEvent()

}