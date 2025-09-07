package parinexus.sample.githubevents.data.local.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import parinexus.sample.githubevents.data.local.entity.LocalEvent
import parinexus.sample.githubevents.data.local.mapper.toRepo
import parinexus.sample.githubevents.data.repository.model.RepoEvent

class RepoEventPagingSourceAdapter(
    private val delegateFactory: () -> PagingSource<Int, LocalEvent>
) : PagingSource<Int, RepoEvent>() {

    private var currentDelegate: PagingSource<Int, LocalEvent>? = null
    private val onDelegateInvalidated: () -> Unit = { this@RepoEventPagingSourceAdapter.invalidate() }

    private fun createAndRegisterDelegate(): PagingSource<Int, LocalEvent> {
        currentDelegate?.unregisterInvalidatedCallback(onDelegateInvalidated)

        val newDelegate = delegateFactory()
        newDelegate.registerInvalidatedCallback(onDelegateInvalidated)
        currentDelegate = newDelegate
        return newDelegate
    }

    override fun getRefreshKey(state: PagingState<Int, RepoEvent>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoEvent> {
        val delegate = currentDelegate?.takeUnless { it.invalid } ?: createAndRegisterDelegate()
        val result = delegate.load(params)
        return when (result) {
            is LoadResult.Page -> LoadResult.Page(
                data = result.data.map { it.toRepo() },
                prevKey = result.prevKey,
                nextKey = result.nextKey,
                itemsBefore = result.itemsBefore,
                itemsAfter = result.itemsAfter
            )
            is LoadResult.Error -> LoadResult.Error(result.throwable)
            is LoadResult.Invalid -> LoadResult.Invalid()
        }
    }
}