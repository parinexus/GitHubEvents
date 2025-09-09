package parinexus.sample.githubevents.data.remote.retrofit

import parinexus.sample.githubevents.data.remote.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import parinexus.sample.githubevents.data.remote.etag.ETagStore
import parinexus.sample.githubevents.data.remote.okhttp.AuthAndHeadersInterceptor
import parinexus.sample.githubevents.data.remote.okhttp.ETagInterceptor

class AppOkHttpClient(
    private val token: String,
    private val userAgent: String,
    private val etagStore: ETagStore

) {

    fun create(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(AuthAndHeadersInterceptor(token, userAgent))
            .addInterceptor(ETagInterceptor(etagStore))

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                    redactHeader("Authorization")
                }
            )
        }

        return clientBuilder.build()
    }
}
