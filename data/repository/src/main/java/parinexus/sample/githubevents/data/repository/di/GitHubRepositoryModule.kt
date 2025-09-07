package parinexus.sample.githubevents.data.repository.di

import parinexus.sample.githubevents.data.repository.GitHubRepositoryImpl
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import parinexus.sample.githubevents.data.repository.datasource.EventsLocalDataSource
import parinexus.sample.githubevents.data.repository.datasource.EventsRemoteDataSource
import parinexus.sample.githubevents.data.repository.datasource.RemoteKeysLocalDataSource
import parinexus.sample.githubevents.data.repository.port.TransactionRunner
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GitHubRepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        eventsRemoteDataSource: EventsRemoteDataSource,
        eventsLocalDataSource: EventsLocalDataSource,
        remoteKeysLocalDataSource: RemoteKeysLocalDataSource,
        transactionRunner: TransactionRunner,
    ): GitHubRepository = GitHubRepositoryImpl(
        eventsRemoteDataSource = eventsRemoteDataSource,
        eventsLocalDataSource = eventsLocalDataSource,
        remoteKeysLocalDataSource = remoteKeysLocalDataSource,
        transactionRunner = transactionRunner
    )
}
