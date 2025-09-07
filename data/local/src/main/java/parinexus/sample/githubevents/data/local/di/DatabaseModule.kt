package parinexus.sample.githubevents.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import parinexus.sample.githubevents.data.local.AppDatabase
import parinexus.sample.githubevents.data.local.dao.EventDao
import parinexus.sample.githubevents.data.local.dao.RemoteKeysDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "github_events.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides @Singleton
    fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()

    @Provides @Singleton
    fun provideRemoteKeysDao(db: AppDatabase): RemoteKeysDao = db.remoteKeysDao()
}