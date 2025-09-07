package parinexus.sample.githubevents.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import parinexus.sample.githubevents.data.repository.model.RepoEvent

class ListBackedPagingSource(
    private val supplier: () -> List<RepoEvent>
) : PagingSource<Int, RepoEvent>() {
    override fun getRefreshKey(state: PagingState<Int, RepoEvent>): Int? = null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoEvent> =
        LoadResult.Page(
            data = supplier(),
            prevKey = null,
            nextKey = null
        )
}