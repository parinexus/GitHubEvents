package parinexus.sample.githubevents.data.remote.di

import parinexus.sample.githubevents.data.repository.datasource.GitHubDataSource
import parinexus.sample.githubevents.data.remote.api.Apis
import parinexus.sample.githubevents.data.remote.datasource.GitHubDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GitHubDataSourceModule {

    @Provides
    @Singleton
    fun provideGitHubDataSource(apis: Apis): GitHubDataSource = GitHubDataSourceImpl(apis)
}
