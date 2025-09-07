package parinexus.sample.githubevents.data.local.robots

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase.assertNull
import parinexus.sample.githubevents.data.local.AppDatabase
import parinexus.sample.githubevents.data.local.dao.RemoteKeysDao
import parinexus.sample.githubevents.data.local.datasource.RemoteKeysLocalDataSourceImpl
import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RemoteKeysLocalDataSourceImplRobot : BaseRobot() {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private val db: AppDatabase = Room.inMemoryDatabaseBuilder(
        context, AppDatabase::class.java
    ).allowMainThreadQueries().build()

    private val dao: RemoteKeysDao = db.remoteKeysDao()
    private val ds = RemoteKeysLocalDataSourceImpl(dao)

    private var loaded: RepoRemoteKeys? = null

    fun close() { db.close() }

    suspend fun insert(vararg keys: RepoRemoteKeys) {
        ds.insertAll(keys.toList())
    }

    suspend fun loadById(id: String) {
        loaded = ds.remoteKeysById(id)
    }

    suspend fun clear() { ds.clear() }

    fun rk(
        id: String,
        prev: Int? = null,
        next: Int? = null
    ): RepoRemoteKeys = RepoRemoteKeys(
        eventId = id,
        prevKey = prev,
        nextKey = next
    )

    fun assertLoaded(id: String, prev: Int?, next: Int?) {
        val v = loaded
        assertNotNull(v, "Expected remote keys for $id")
        assertEquals(id, v.eventId)
        assertEquals(prev, v.prevKey)
        assertEquals(next, v.nextKey)
    }

    suspend fun assertNullById(id: String) {
        val v = ds.remoteKeysById(id)
        assertNull(v)
    }
}