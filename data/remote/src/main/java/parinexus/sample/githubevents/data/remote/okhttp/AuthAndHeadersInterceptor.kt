package parinexus.sample.githubevents.data.remote.okhttp

import okhttp3.Interceptor
import okhttp3.Response

class AuthAndHeadersInterceptor(
    private val token: String,
    private val userAgent: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", userAgent)
            .header("X-GitHub-Api-Version", "2022-11-28")

        if (token.isNotBlank()) {
            builder.header("Authorization", "Bearer $token")
        }
        return chain.proceed(builder.build())
    }
}
