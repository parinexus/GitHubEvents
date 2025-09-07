package parinexus.sample.githubevents.data.local.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import parinexus.sample.githubevents.data.local.entity.LocalEvent
import parinexus.sample.githubevents.data.local.mapper.toRepo
import parinexus.sample.githubevents.data.repository.model.RepoEvent

class RepoEventPagingSourceAdapter(
    private val delegateFactory: () -> PagingSource<Int, LocalEvent>
) : PagingSource<Int, RepoEvent>() {

    private var delegate: PagingSource<Int, LocalEvent>? = null
    private val onDelegateInvalidated: () -> Unit = { this@RepoEventPagingSourceAdapter.invalidate() }

    private fun obtainDelegate(): PagingSource<Int, LocalEvent> {
        delegate?.unregisterInvalidatedCallback(onDelegateInvalidated)

        val d = delegateFactory()
        d.registerInvalidatedCallback(onDelegateInvalidated)
        delegate = d
        return d
    }

    override fun getRefreshKey(state: PagingState<Int, RepoEvent>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoEvent> {
        val d = delegate?.takeUnless { it.invalid } ?: obtainDelegate()
        return when (val res = d.load(params)) {
            is LoadResult.Page -> LoadResult.Page(
                data = res.data.map { it.toRepo() },
                prevKey = res.prevKey,
                nextKey = res.nextKey,
                itemsBefore = res.itemsBefore,
                itemsAfter = res.itemsAfter
            )
            is LoadResult.Error -> LoadResult.Error(res.throwable)
            is LoadResult.Invalid -> LoadResult.Invalid()
        }
    }
}