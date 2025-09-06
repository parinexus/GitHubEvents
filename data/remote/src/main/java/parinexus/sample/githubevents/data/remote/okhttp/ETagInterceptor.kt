package parinexus.sample.githubevents.data.remote.okhttp

import okhttp3.Interceptor
import okhttp3.Response
import parinexus.sample.githubevents.data.remote.etag.ETagStore

class ETagInterceptor(
    private val etagStore: ETagStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val key = req.url.toString()

        val withIfNoneMatch = etagStore.get(key)?.let { etag ->
            req.newBuilder().header("If-None-Match", etag).build()
        } ?: req

        val resp = chain.proceed(withIfNoneMatch)

        resp.header("ETag")?.let { etag ->
            if (resp.code == 200) etagStore.put(key, etag)
        }
        return resp
    }
}
