package parinexus.sample.githubevents.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import parinexus.sample.githubevents.data.local.entity.LocalEvent

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY createdAtEpochMillis DESC")
    fun pagingSource(): PagingSource<Int, LocalEvent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertAll(items: List<LocalEvent>)

    @Query("SELECT * FROM events WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): LocalEvent?

    @Query("DELETE FROM events")
    suspend fun clearAll()

    @Query("SELECT createdAtEpochMillis FROM events ORDER BY createdAtEpochMillis DESC LIMIT 1")
    suspend fun latestCreatedAt(): Long?
}