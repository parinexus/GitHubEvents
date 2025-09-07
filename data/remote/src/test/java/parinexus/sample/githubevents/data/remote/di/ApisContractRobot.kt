package parinexus.sample.githubevents.data.remote.di

import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.Assert.assertTrue
import parinexus.sample.githubevents.data.remote.api.Apis
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals

class ApisContractRobot(
    private val server: MockWebServer,
    @Suppress("unused")
    private val apis: Apis
) : BaseRobot() {

    private lateinit var recorded: RecordedRequest

    fun captureRequest() {
        recorded = server.takeRequest()
    }

    fun assertPathAndQuery() {
        assertEquals("/events", recorded.requestUrl?.encodedPath)
        assertEquals("30", recorded.requestUrl?.queryParameter("per_page"))
        assertEquals("2",  recorded.requestUrl?.queryParameter("page"))
    }

    fun assertHeaders() {
        assertEquals("application/vnd.github+json", recorded.getHeader("Accept"))
        assertEquals("2022-11-28", recorded.getHeader("X-GitHub-Api-Version"))
        assertTrue((recorded.getHeader("User-Agent") ?: "").isNotBlank())
    }
}