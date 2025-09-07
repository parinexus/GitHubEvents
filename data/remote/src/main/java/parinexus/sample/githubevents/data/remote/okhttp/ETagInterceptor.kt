package parinexus.sample.githubevents.data.remote.okhttp

import okhttp3.Interceptor
import okhttp3.Response
import parinexus.sample.githubevents.data.remote.etag.ETagStore
import okhttp3.Request

class ETagInterceptor(
    private val etagStore: ETagStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val isGetRequest = originalRequest.method.equals("GET", ignoreCase = true)
        val etagKey = originalRequest.url.toString()

        val finalRequest: Request =
            if (isGetRequest) originalRequest.withIfNoneMatch(etagStore, etagKey)
            else originalRequest

        val response = chain.proceed(finalRequest)

        response.header(HEADER_ETAG)?.let { etag ->
            if (response.code == 200 && isGetRequest) {
                etagStore.put(etagKey, etag)
            }
        }
        return response
    }
}

private fun Request.withIfNoneMatch(store: ETagStore, key: String): Request {
    val etag = store.get(key) ?: return this
    return newBuilder().header(HEADER_IF_NONE_MATCH, etag).build()
}

private const val HEADER_IF_NONE_MATCH = "If-None-Match"
private const val HEADER_ETAG = "ETag"
