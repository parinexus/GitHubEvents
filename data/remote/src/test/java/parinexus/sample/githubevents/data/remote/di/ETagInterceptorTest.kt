package parinexus.sample.githubevents.data.remote.di

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import parinexus.sample.githubevents.data.remote.etag.ETagStore
import parinexus.sample.githubevents.data.remote.okhttp.ETagInterceptor
import parinexus.sample.githubevents.libraries.test.BaseRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import kotlin.test.assertEquals

class ETagInterceptorTest {

    private val store = object : ETagStore {
        private val map = mutableMapOf<String, String>()
        override fun get(key: String) = map[key]
        override fun put(key: String, value: String) { map[key] = value }
    }

    private fun client(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(ETagInterceptor(store))
        .build()

    @Test
    fun `GIVEN stored etag WHEN same url called THEN adds If-None-Match AND stores new on 200`() = RUN_UNIT_TEST(object : BaseRobot() {}) {
        val server = MockWebServer().apply { start() }
        try {
            val url = server.url("/events?per_page=30&page=1").toString()
            store.put(url, "\"etag-1\"")

            GIVEN {
                server.enqueue(
                    MockResponse()
                        .setResponseCode(200)
                        .setHeader("ETag", "\"etag-2\"")
                        .setBody("[]")
                )
            }

            WHEN {
                val req = Request.Builder().url(url).get().build()
                client().newCall(req).execute().use {}
            }

            THEN {
                val recorded = server.takeRequest()
                assertEquals("\"etag-1\"", recorded.getHeader("If-None-Match"))
            }
            AND {
                assertEquals("\"etag-2\"", store.get(url))
            }
        } finally {
            server.shutdown()
        }
    }
}
