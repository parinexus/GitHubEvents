package parinexus.sample.githubevents.data.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.CompletableDeferred
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.FakeEventsLocalDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent

class TestLocalWrapper(
    private val delegate: FakeEventsLocalDataSource
) : EventsLocalDataSource {

    var firstUpsertSignal: CompletableDeferred<Unit>? = null

    override fun pagingSource(): PagingSource<Int, RepoEvent> = delegate.pagingSource()

    override suspend fun upsertAll(items: List<RepoEvent>) {
        delegate.upsertAll(items)
        firstUpsertSignal?.complete(Unit)
    }

    override suspend fun getById(id: String): RepoEvent? = delegate.getById(id)
    override suspend fun latestCreatedAt(): Long? = delegate.latestCreatedAt()
    override suspend fun clearAll() = delegate.clearAll()

    suspend fun seed(vararg events: RepoEvent) {
        upsertAll(events.toList())
    }

    suspend fun idsByCreatedDesc(): List<String> {
        val ps = pagingSource()
        val loaded = ps.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 100,
                placeholdersEnabled = false
            )
        )
        return when (loaded) {
            is PagingSource.LoadResult.Page -> loaded.data.map { it.id }
            is PagingSource.LoadResult.Error -> emptyList()
            is PagingSource.LoadResult.Invalid -> emptyList()
        }
    }

    suspend fun size(): Int = idsByCreatedDesc().size
}