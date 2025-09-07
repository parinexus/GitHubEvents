package parinexus.sample.githubevents.data.local.datasource

import androidx.paging.PagingSource
import parinexus.sample.githubevents.data.local.dao.EventDao
import parinexus.sample.githubevents.data.local.mapper.toLocal
import parinexus.sample.githubevents.data.local.mapper.toRepo
import parinexus.sample.githubevents.data.local.paging.RepoEventPagingSourceAdapter
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import javax.inject.Inject

class EventsLocalDataSourceImpl @Inject constructor(
    private val dao: EventDao
) : EventsLocalDataSource {

    override fun pagingSource(): PagingSource<Int, RepoEvent> =
        RepoEventPagingSourceAdapter { dao.pagingSource() }

    override suspend fun upsertAll(items: List<RepoEvent>) =
        dao.upsertAll(items.map { it.toLocal() })

    override suspend fun getById(id: String): RepoEvent? =
        dao.getById(id)?.toRepo()

    override suspend fun latestCreatedAt(): Long? =
        dao.latestCreatedAt()

    override suspend fun clearAll() = dao.clearAll()
}

