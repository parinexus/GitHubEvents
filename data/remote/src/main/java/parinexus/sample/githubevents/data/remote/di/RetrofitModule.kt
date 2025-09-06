package parinexus.sample.githubevents.data.remote.di

import parinexus.sample.githubevents.data.remote.BuildConfig
import parinexus.sample.githubevents.data.remote.retrofit.AppRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = AppRetrofit(
        okHttpClient = okHttpClient,
        httpUrl = BuildConfig.BASE_URL.toHttpUrl()
    ).create()
}
