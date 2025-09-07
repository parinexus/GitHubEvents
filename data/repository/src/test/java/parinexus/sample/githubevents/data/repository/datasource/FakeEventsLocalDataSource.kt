package parinexus.sample.githubevents.data.repository.datasource

import androidx.paging.PagingSource
import parinexus.sample.githubevents.data.repository.ListBackedPagingSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import kotlin.collections.forEach

class FakeEventsLocalDataSource : EventsLocalDataSource {
    private val storage = mutableListOf<RepoEvent>()
    private var current: ListBackedPagingSource? = null

    override fun pagingSource(): PagingSource<Int, RepoEvent> =
        ListBackedPagingSource {
            storage.sortedByDescending { it.createdAtEpochMillis }
        }.also { current = it }

    override suspend fun upsertAll(items: List<RepoEvent>) {
        val seen = storage.associateBy { it.id }.toMutableMap()
        items.forEach { if (it.id !in seen) storage += it }
        current?.invalidate()
    }

    override suspend fun getById(id: String): RepoEvent? = storage.firstOrNull { it.id == id }

    override suspend fun latestCreatedAt(): Long? = storage.maxOfOrNull { it.createdAtEpochMillis }

    override suspend fun clearAll() { storage.clear(); current?.invalidate() }
}