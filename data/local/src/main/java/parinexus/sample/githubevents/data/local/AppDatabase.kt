package parinexus.sample.githubevents.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import parinexus.sample.githubevents.data.local.dao.EventDao
import parinexus.sample.githubevents.data.local.dao.RemoteKeysDao
import parinexus.sample.githubevents.data.local.entity.LocalEvent
import parinexus.sample.githubevents.data.local.entity.RemoteKeys

@Database(entities = [LocalEvent::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}