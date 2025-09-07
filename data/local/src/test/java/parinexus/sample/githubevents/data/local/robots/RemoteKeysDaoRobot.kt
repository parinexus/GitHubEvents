package parinexus.sample.githubevents.data.local.robots

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import parinexus.sample.githubevents.data.local.AppDatabase
import parinexus.sample.githubevents.data.local.dao.RemoteKeysDao
import parinexus.sample.githubevents.data.local.entity.RemoteKeys
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class RemoteKeysDaoRobot : BaseRobot() {
    private val db: AppDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java
    ).allowMainThreadQueries().build()
    private val dao: RemoteKeysDao = db.remoteKeysDao()

    fun close() = db.close()

    suspend fun insert(vararg keys: RemoteKeys) { dao.insertAll(keys.toList()) }
    suspend fun clear() { dao.clear() }
    suspend fun loadById(id: String) {}

    suspend fun assertPrevNext(id: String, prev: Int?, next: Int?) {
        val k = dao.remoteKeysById(id)
        assertNotNull(k)
        assertEquals(prev, k.prevKey)
        assertEquals(next, k.nextKey)
    }
    suspend fun assertNullById(id: String) {
        assertNull(dao.remoteKeysById(id))
    }
    suspend fun assertIdExists(id: String) {
        assertNotNull(dao.remoteKeysById(id))
    }
}