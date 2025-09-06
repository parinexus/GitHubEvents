package parinexus.sample.githubevents.data.remote.datasource

import parinexus.sample.githubevents.libraries.test.BaseRobot
import parinexus.sample.githubevents.libraries.test.dsl.AND
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import androidx.paging.PagingSource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertNotSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import parinexus.sample.githubevents.data.repository.datasource.GitHubDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class GitHubDataSourceImplHiltTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var gitHubDataSource: GitHubDataSource
    @Inject lateinit var mockWebServer: MockWebServer

    private lateinit var robot: Robot

    @Before
    fun setup() {
        hiltRule.inject()

        robot = Robot(mockWebServer, gitHubDataSource)
    }

    @org.junit.After
    fun tearDown() {
        try { mockWebServer.shutdown() } catch (_: Exception) {}
    }

    @Test
    fun test_page1_getEvents() = RUN_UNIT_TEST(robot) {
        GIVEN { mockEventsApiPage1() }
        WHEN  { loadEvents(page = null) }
        THEN  { checkLoadResultIsAPage() }
        AND   { checkPage1Result() }
    }

    @Test
    fun test_page2_getEvents() = RUN_UNIT_TEST(robot) {
        GIVEN { mockEventsApiPage2() }
        WHEN  { loadEvents(page = 2) }
        THEN  { checkLoadResultIsAPage() }
        AND   { checkPage2Result() }
    }

    @Test
    fun test_page3_getEvents() = RUN_UNIT_TEST(robot) {
        GIVEN { mockEventsApiPage3() }
        WHEN  { loadEvents(page = 3) }
        THEN  { checkLoadResultIsAPage() }
        AND   { checkPage3Result() }
    }

    @Test
    fun test_getEvents_returnsNewInstanceEachCall() = RUN_UNIT_TEST(robot) {
        WHEN { captureTwoPagingSources() }
        THEN { checkDistinctInstances() }
    }


    private class Robot(
        private val mockWebServer: MockWebServer,
        private val gitHubDataSource: GitHubDataSource,
    ) : BaseRobot() {

        private var loadResult: PagingSource.LoadResult<Int, RepoEvent>? = null
        private var first: PagingSource<Int, RepoEvent>? = null
        private var second: PagingSource<Int, RepoEvent>? = null

        private fun linkHeaderFor(page: Int): String {
            val parts = mutableListOf<String>()
            val base = "/events?per_page=30&page="

            if (page > 1) {
                parts += "<${mockWebServer.url("$base${page - 1}")}>; rel=\"prev\""
            }
            parts += "<${mockWebServer.url("${base}1")}>; rel=\"first\""

            val next = page + 1
            parts += "<${mockWebServer.url("$base$next")}>; rel=\"next\""

            parts += "<${mockWebServer.url("${base}4")}>; rel=\"last\""

            return parts.joinToString(", ")
        }

        private fun enqueueWithLink(fileName: String, page: Int, code: Int = 200) {
            val body = loadJson(fileName)
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Link", linkHeaderFor(page))
                    .setBody(body)
            )
        }

        private fun loadJson(fileName: String): String {
            val cl = this::class.java.classLoader
            val stream = cl?.getResourceAsStream(fileName)
                ?: error("Missing test resource: $fileName (place under src/test/resources)")
            return stream.bufferedReader().use { it.readText() }
        }

        fun mockEventsApiPage1() {
            enqueueWithLink(fileName = "get-events-page1-200.json", page = 1)
        }

        fun mockEventsApiPage2() {
            enqueueWithLink(fileName = "get-events-page2-200.json", page = 2)
        }

        fun mockEventsApiPage3() {
            enqueueWithLink(fileName = "get-events-page3-200.json", page = 3)
        }


        fun checkLoadResultIsAPage() {
            val r = loadResult
            when (r) {
                is PagingSource.LoadResult.Page -> assertTrue(true)
                is PagingSource.LoadResult.Error -> throw AssertionError(
                    "Expected Page, got Error: ${r.throwable.message}", r.throwable
                )
                else -> throw AssertionError("Expected Page, was $r")
            }
        }

        fun checkPage1Result() {
            val page = loadResult as PagingSource.LoadResult.Page<Int, RepoEvent>
            assertEquals(30, page.data.size)
            assertEquals(null, page.prevKey)
            assertEquals(2, page.nextKey)
            assertEquals("A", page.data[0].actor.login)
        }

        fun checkPage2Result() {
            val page = loadResult as PagingSource.LoadResult.Page<Int, RepoEvent>
            assertEquals(30, page.data.size)
            assertEquals(1, page.prevKey)
            assertEquals(3, page.nextKey)
            assertEquals("zonble", page.data[0].actor.login)
        }

        fun checkPage3Result() {
            val page = loadResult as PagingSource.LoadResult.Page<Int, RepoEvent>
            assertEquals(30, page.data.size)
            assertEquals(2, page.prevKey)
            assertEquals(4, page.nextKey)
            assertEquals("hathibelagal", page.data[0].actor.login)
        }

        suspend fun loadEvents(page: Int?) {
            loadResult = gitHubDataSource.getEvents().load(
                PagingSource.LoadParams.Refresh(
                    key = page,
                    loadSize = 30,
                    placeholdersEnabled = false,
                )
            )
        }

        fun captureTwoPagingSources() {
            first = gitHubDataSource.getEvents()
            second = gitHubDataSource.getEvents()
        }

        fun checkDistinctInstances() {
            assertNotSame(first, second)
        }
    }
}