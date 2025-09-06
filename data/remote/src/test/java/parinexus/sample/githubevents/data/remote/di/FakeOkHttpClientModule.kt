package parinexus.sample.githubevents.data.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import parinexus.sample.githubevents.data.remote.etag.ETagStore
import parinexus.sample.githubevents.data.remote.etag.MemoryETagStore
import parinexus.sample.githubevents.data.remote.retrofit.AppOkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [OkHttpClientModule::class]
)
object FakeOkHttpClientModule {

    @Provides @Singleton
    fun provideETagStore(): ETagStore = MemoryETagStore()

    @Provides @Singleton @Named("github_token")
    fun provideGithubToken(): String = "TEST_TOKEN"

    @Provides @Singleton @Named("github_user_agent")
    fun provideUserAgent(): String = "github-events-compose (test)"

    @Provides @Singleton
    fun provideOkHttpClient(
        etagStore: ETagStore,
        @Named("github_token") token: String,
        @Named("github_user_agent") userAgent: String
    ): OkHttpClient {
        val base = AppOkHttpClient(
            token = token,
            userAgent = userAgent,
            etagStore = etagStore
        ).create()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
            redactHeader("Authorization")
            redactHeader("Cookie")
        }

        return base.newBuilder()
            .addInterceptor(logging)
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }
}