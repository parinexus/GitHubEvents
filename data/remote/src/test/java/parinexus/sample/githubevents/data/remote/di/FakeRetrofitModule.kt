package parinexus.sample.githubevents.data.remote.di

import parinexus.sample.githubevents.data.remote.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RetrofitModule::class]
)
object FakeRetrofitModule {

    @Singleton
    @Provides
    fun mockWebServer(): MockWebServer = MockWebServer()

    @Singleton
    @Provides
    fun provideRetrofit(
        mockWebServer: MockWebServer,
        okHttpClient: OkHttpClient
    ): Retrofit = AppRetrofit(okHttpClient, mockWebServer.url("/")).create()
}
