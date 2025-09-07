package parinexus.sample.githubevents.data.local.robots

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import parinexus.sample.githubevents.data.local.AppDatabase
import parinexus.sample.githubevents.data.local.dao.EventDao
import parinexus.sample.githubevents.data.local.entity.LocalEvent
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EventDaoRobot : BaseRobot() {
    private val db: AppDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).allowMainThreadQueries().build()
    private val dao: EventDao = db.eventDao()

    private var loaded: LocalEvent? = null
    private var page: PagingSource.LoadResult.Page<Int, LocalEvent>? = null
    private var latest: Long? = null

    fun close() = db.close()

    suspend fun insert(vararg events: LocalEvent) {
        dao.upsertAll(events.toList())
    }

    suspend fun insertIgnore(vararg events: LocalEvent) {
        dao.upsertAll(events.toList())
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun loadById(id: String) {
        loaded = dao.getById(id)
    }

    suspend fun loadFirstPage(loadSize: Int) {
        val res = dao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null, loadSize = loadSize, placeholdersEnabled = false
            )
        )
        require(res is PagingSource.LoadResult.Page)
        page = res
    }

    suspend fun queryLatestCreatedAt() {
        latest = dao.latestCreatedAt()
    }

    fun assertLoadedId(expected: String) {
        assertEquals(expected, loaded?.id)
    }

    fun assertLoadedActor(expected: String) {
        assertEquals(expected, loaded?.actorLogin)
    }

    suspend fun assertNullById(id: String) {
        assertNull(dao.getById(id))
    }

    suspend fun assertActorById(id: String, expected: String) {
        assertEquals(expected, dao.getById(id)?.actorLogin)
    }

    suspend fun assertIdExists(id: String) {
        assertNotNull(dao.getById(id))
    }

    fun assertPageOrder(expectedIds: List<String>) {
        val ids = requireNotNull(page).data.map { it.id }
        assertEquals(expectedIds, ids)
    }

    fun assertPageSize(expected: Int) {
        assertEquals(expected, requireNotNull(page).data.size)
    }

    suspend fun assertPagingEmpty() {
        val res = dao.pagingSource().load(
            PagingSource.LoadParams.Refresh(
                key = null, loadSize = 10, placeholdersEnabled = false
            )
        )
        require(res is PagingSource.LoadResult.Page)
        assertTrue(res.data.isEmpty())
    }

    fun assertLatest(expected: Long) {
        assertEquals(expected, latest)
    }

    fun assertLatestNotNull() {
        assertNotNull(latest)
    }

    fun e(
        id: String = "id",
        type: String = "PushEvent",
        actorLogin: String = id,
        avatarUrl: String = "https://avatars.githubusercontent.com/u/1",
        repoName: String = "octo/repo",
        repoUrl: String = "https://github.com/octo/repo",
        createdAt: Long = 1_000L
    ) = LocalEvent(
        id = id,
        type = type,
        actorLogin = actorLogin,
        actorAvatarUrl = avatarUrl,
        repoName = repoName,
        repoUrl = repoUrl,
        createdAtEpochMillis = createdAt
    )

}