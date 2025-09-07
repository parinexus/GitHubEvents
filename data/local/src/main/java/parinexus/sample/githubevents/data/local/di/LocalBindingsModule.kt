package parinexus.sample.githubevents.data.local.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import parinexus.sample.githubevents.data.local.datasource.EventsLocalDataSourceImpl
import parinexus.sample.githubevents.data.local.datasource.RemoteKeysLocalDataSourceImpl
import parinexus.sample.githubevents.data.local.datasource.RoomTransactionRunner
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.RemoteKeysLocalDataSource
import parinexus.sample.githubevents.data.repository.port.TransactionRunner

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalBindings {

    @Binds
    abstract fun bindEventsLocal(impl: EventsLocalDataSourceImpl): EventsLocalDataSource

    @Binds
    abstract fun bindRemoteKeysLocal(impl: RemoteKeysLocalDataSourceImpl): RemoteKeysLocalDataSource

    @Binds
    abstract fun bindTransactionRunner(impl: RoomTransactionRunner): TransactionRunner
}