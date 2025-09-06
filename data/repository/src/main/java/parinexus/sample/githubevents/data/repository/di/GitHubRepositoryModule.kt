package parinexus.sample.githubevents.data.repository.di

import parinexus.sample.githubevents.data.repository.GitHubRepositoryImpl
import parinexus.sample.githubevents.data.repository.datasource.GitHubDataSource
import parinexus.sample.githubevents.domain.repository.GitHubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import parinexus.sample.githubevents.data.repository.store.EventStore
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GitHubRepositoryModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(
        gitHubDataSource: GitHubDataSource,
        eventStore: EventStore
    ): GitHubRepository = GitHubRepositoryImpl(
        gitHubDataSource = gitHubDataSource,
        eventStore = eventStore
    )
}
