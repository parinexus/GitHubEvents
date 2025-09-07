package parinexus.sample.githubevents.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import parinexus.sample.githubevents.data.local.entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE eventId = :id")
    suspend fun remoteKeysById(id: String): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clear()
}