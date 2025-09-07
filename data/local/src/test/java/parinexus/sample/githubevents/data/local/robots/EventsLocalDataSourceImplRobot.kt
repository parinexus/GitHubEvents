package parinexus.sample.githubevents.data.local.robots

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertNull
import parinexus.sample.githubevents.data.local.AppDatabase
import parinexus.sample.githubevents.data.local.dao.EventDao
import parinexus.sample.githubevents.data.local.datasource.EventsLocalDataSourceImpl
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EventsLocalDataSourceImplRobot : BaseRobot() {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val db: AppDatabase = Room.inMemoryDatabaseBuilder(
        context, AppDatabase::class.java
    ).allowMainThreadQueries().build()

    private val dao: EventDao = db.eventDao()
    private val ds = EventsLocalDataSourceImpl(dao)

    private var loaded: RepoEvent? = null
    private var latest: Long? = null
    private var page: PagingSource.LoadResult.Page<Int, RepoEvent>? = null

    fun close() { db.close() }

    suspend fun upsert(vararg items: RepoEvent) {
        ds.upsertAll(items.toList())
    }

    suspend fun loadById(id: String) {
        loaded = ds.getById(id)
    }

    suspend fun loadFirstPage(loadSize: Int) {
        val src: PagingSource<Int, RepoEvent> = ds.pagingSource()
        val res = src.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = loadSize,
                placeholdersEnabled = false
            )
        )
        page = res as? PagingSource.LoadResult.Page
            ?: error("Expected Page, was $res")
    }

    suspend fun queryLatestCreatedAt() {
        latest = ds.latestCreatedAt()
    }

    suspend fun clearAll() {
        ds.clearAll()
    }

    fun re(
        id: String = "id",
        type: String = "PushEvent",
        actorLogin: String = "user_$id",
        actorAvatarUrl: String = "https://avatar.example/$id.png",
        repoName: String? = "owner/repo",
        repoUrl: String? = "https://api.github.com/repos/owner/repo",
        createdAt: Long = 0L
    ): RepoEvent = RepoEvent(
        id = id,
        type = type,
        actorLogin = actorLogin,
        actorAvatarUrl = actorAvatarUrl,
        repoName = repoName,
        repoUrl = repoUrl,
        createdAtEpochMillis = createdAt
    )

    fun assertLoadedId(expected: String) {
        assertEquals(expected, loaded?.id)
    }

    fun assertLoadedActor(expected: String) {
        assertEquals(expected, loaded?.actorLogin)
    }

    suspend fun assertNullById(id: String) {
        val v = ds.getById(id)
        assertNull(v)
    }

    suspend fun assertIdExists(id: String) {
        val v = ds.getById(id)
        assertNotNull(v)
    }

    suspend fun assertActorById(id: String, expected: String) {
        val v = ds.getById(id)
        assertNotNull(v, "Expected $id to exist")
        assertEquals(expected, v.actorLogin)
    }

    fun assertPageOrder(expectedIds: List<String>) {
        val ids = page?.data?.map { it.id } ?: emptyList()
        assertEquals(expectedIds, ids)
    }

    fun assertPageSize(expected: Int) {
        val size = page?.data?.size ?: 0
        assertEquals(expected, size)
    }

    fun assertLatest(expected: Long) {
        assertEquals(expected, latest)
    }

    fun assertLatestNotNull() {
        assertTrue(latest != null)
    }

    suspend fun assertPagingEmpty() {
        loadFirstPage(loadSize = 10)
        assertEquals(0, page?.data?.size ?: -1)
    }
}