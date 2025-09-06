package parinexus.sample.githubevents.data.remote.di

import parinexus.sample.githubevents.data.remote.retrofit.AppOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import parinexus.sample.githubevents.data.remote.BuildConfig
import parinexus.sample.githubevents.data.remote.etag.ETagStore
import parinexus.sample.githubevents.data.remote.etag.MemoryETagStore
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    @Provides
    @Singleton
    fun provideETagStore(): ETagStore = MemoryETagStore()

    @Provides
    @Singleton
    @Named("github_token")
    fun provideGithubToken(): String = BuildConfig.GITHUB_TOKEN

    @Provides
    @Singleton
    @Named("github_user_agent")
    fun provideUserAgent(): String =
        "github-events-compose (${BuildConfig.APPLICATION_ID})"

    @Provides
    @Singleton
    fun provideOkHttpClint(
        etagStore: ETagStore,
        @Named("github_token") token: String,
        @Named("github_user_agent") userAgent: String
    ): OkHttpClient = AppOkHttpClient(
        token = token,
        userAgent = userAgent,
        etagStore = etagStore
    ).create()
}
