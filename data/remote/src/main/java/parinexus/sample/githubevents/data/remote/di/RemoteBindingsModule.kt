package parinexus.sample.githubevents.data.remote.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import parinexus.sample.githubevents.data.remote.datasource.EventsRemoteDataSourceImpl
import parinexus.sample.githubevents.data.repository.datasource.EventsRemoteDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteBindings {

    @Binds
    abstract fun bindEventsRemote(impl: EventsRemoteDataSourceImpl): EventsRemoteDataSource
}