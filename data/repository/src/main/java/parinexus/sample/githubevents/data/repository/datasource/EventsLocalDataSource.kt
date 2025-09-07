package parinexus.sample.githubevents.data.repository.datasource

import androidx.paging.PagingSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent

interface EventsLocalDataSource {
    fun pagingSource(): PagingSource<Int, RepoEvent>
    suspend fun upsertAll(items: List<RepoEvent>)
    suspend fun getById(id: String): RepoEvent?
    suspend fun latestCreatedAt(): Long?
    suspend fun clearAll()
}