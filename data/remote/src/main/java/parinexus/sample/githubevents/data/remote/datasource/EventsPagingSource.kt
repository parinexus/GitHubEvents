package parinexus.sample.githubevents.data.remote.datasource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import parinexus.sample.githubevents.data.remote.api.Apis
import parinexus.sample.githubevents.data.remote.entity.EventType
import parinexus.sample.githubevents.data.remote.mapper.toRepo
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import retrofit2.HttpException
import java.io.IOException

class EventsPagingSource(
    private val apis: Apis,
) : PagingSource<Int, RepoEvent>() {

    override fun getRefreshKey(state: PagingState<Int, RepoEvent>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoEvent> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val resp = apis.getEvents(limit = params.loadSize, page = page)

            when (resp.code()) {
                304 -> {
                    LoadResult.Page(
                        data = emptyList(),
                        prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                        nextKey = null
                    )
                }

                in 200..299 -> {
                    val body = resp.body().orEmpty()
                    val filtered = body.filter { it.type in EventType.subscribedTypes }

                    val events: List<RepoEvent> = filtered.map { it.toRepo() }

                    val link = resp.headers()["Link"]
                    val nextKeyFromHeader = parseNextPage(link)
                    val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1

                    LoadResult.Page(
                        data = events,
                        prevKey = prevKey,
                        nextKey = nextKeyFromHeader
                    )
                }

                else -> throw HttpException(resp)
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    companion object Companion {
        private const val STARTING_PAGE_INDEX = 1

        private fun parseNextPage(link: String?): Int? {
            if (link.isNullOrBlank()) return null
            val nextPart = link.split(",")
                .firstOrNull { it.contains("rel=\"next\"") }
                ?: return null

            val url = nextPart.substringAfter("<").substringBefore(">")
            val pageStr = runCatching { Uri.parse(url).getQueryParameter("page") }.getOrNull()
            return pageStr?.toIntOrNull()
        }
    }
}