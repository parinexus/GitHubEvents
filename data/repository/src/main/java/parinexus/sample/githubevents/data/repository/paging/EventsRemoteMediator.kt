package parinexus.sample.githubevents.data.repository.paging

import androidx.paging.*
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.EventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.datasource.RemoteKeysLocalDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys
import parinexus.sample.githubevents.data.repository.port.TransactionRunner
import retrofit2.HttpException
import java.io.IOException
import androidx.core.net.toUri

private const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class EventsRemoteMediator(
    private val remote: EventsRemoteDataSource,
    private val local: EventsLocalDataSource,
    private val keys: RemoteKeysLocalDataSource,
    private val tx: TransactionRunner,
    private val pageSize: Int
) : RemoteMediator<Int, RepoEvent>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoEvent>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE_INDEX
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val last = state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
                    val next = last?.let { keys.remoteKeysById(it.id)?.nextKey }
                    next ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val resp = remote.getEvents(limit = pageSize, page = page)
            when (resp.code) {
                304 -> return MediatorResult.Success(endOfPaginationReached = true)
                in 200..299 -> Unit
                else -> throw HttpException(retrofit2.Response.success(null))
            }

            val locals = resp.items
            val nextKeyFromHeader = parseNextPage(resp.linkHeader)
            val end = nextKeyFromHeader == null || locals.isEmpty()

            tx {
                if (loadType == LoadType.REFRESH) {
                    keys.clear()
                    local.clearAll()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                keys.insertAll(locals.map { e -> RepoRemoteKeys(e.id, prevKey, nextKeyFromHeader) })
                local.upsertAll(locals)
            }

            MediatorResult.Success(endOfPaginationReached = end)
        } catch (io: IOException) {
            MediatorResult.Error(io)
        } catch (he: HttpException) {
            MediatorResult.Error(he)
        }
    }

    private fun parseNextPage(link: String?): Int? {
        if (link.isNullOrBlank()) return null
        val nextPart = link.split(",").firstOrNull { it.contains("rel=\"next\"") } ?: return null
        val url = nextPart.substringAfter("<").substringBefore(">")
        val pageStr = runCatching { url.toUri().getQueryParameter("page") }.getOrNull()
        return pageStr?.toIntOrNull()
    }
}