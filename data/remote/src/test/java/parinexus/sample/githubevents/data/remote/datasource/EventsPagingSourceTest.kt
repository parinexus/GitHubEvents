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
import parinexus.sample.githubevents.data.remote.api.Apis
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class EventsPagingSourceHiltTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var apis: Apis
    @Inject lateinit var mockWebServer: MockWebServer

    private lateinit var robot: Robot

    @Before
    fun setup() {
        hiltRule.inject()
        robot = Robot(mockWebServer, apis)
    }

    @org.junit.After
    fun tearDown() {
        try { mockWebServer.shutdown() } catch (_: Exception) {}
    }

    @Test
    fun page1_success_withNext() = RUN_UNIT_TEST(robot) {
        GIVEN { enqueuePage(file = "get-events-page1-200.json", page = 1, withLink = true) }
        WHEN  { load(page = null, pageSize = 30) }
        THEN  { assertIsPage() }
        AND   { assertPageData(size = 30, prev = null, next = 2, firstActor = "A") }
    }

    @Test
    fun page2_success_withPrevAndNext() = RUN_UNIT_TEST(robot) {
        GIVEN { enqueuePage(file = "get-events-page2-200.json", page = 2, withLink = true) }
        WHEN  { load(page = 2, pageSize = 30) }
        THEN  { assertIsPage() }
        AND   { assertPageData(size = 30, prev = 1, next = 3, firstActor = "zonble") }
    }

    @Test
    fun page3_success_withPrevAndNext() = RUN_UNIT_TEST(robot) {
        GIVEN { enqueuePage(file = "get-events-page3-200.json", page = 3, withLink = true) }
        WHEN  { load(page = 3, pageSize = 30) }
        THEN  { assertIsPage() }
        AND   { assertPageData(size = 30, prev = 2, next = 4, firstActor = "hathibelagal") }
    }

    @Test
    fun success_withoutLink_header_setsNextNull() = RUN_UNIT_TEST(robot) {
        GIVEN { enqueuePage(file = "get-events-page1-200.json", page = 1, withLink = false) }
        WHEN  { load(page = 1, pageSize = 30) }
        THEN  { assertIsPage() }
        AND   { assertPageData(size = 30, prev = null, next = null, firstActor = "A") }
    }

    @Test
    fun notModified_304_returnsEmpty_andNextNull() = RUN_UNIT_TEST(robot) {
        GIVEN { enqueue304(page = 2) }
        WHEN  { load(page = 2, pageSize = 30) }
        THEN  { assertIsPage() }
        AND   { assertPageData(size = 0, prev = 1, next = null, firstActor = null) }
    }

    @Test
    fun newInstance_eachTime() = RUN_UNIT_TEST(robot) {
        WHEN { createTwoPagingSources() }
        THEN { assertDistinctInstances() }
    }

    private class Robot(
        private val server: MockWebServer,
        private val apis: Apis
    ) : BaseRobot() {

        private var pagingSource1: EventsPagingSource? = null
        private var pagingSource2: EventsPagingSource? = null
        private var loadResult: PagingSource.LoadResult<Int, RepoEvent>? = null

        fun createTwoPagingSources() {
            pagingSource1 = EventsPagingSource(apis)
            pagingSource2 = EventsPagingSource(apis)
        }

        fun assertDistinctInstances() {
            assertNotSame(pagingSource1, pagingSource2)
        }

        suspend fun load(page: Int?, pageSize: Int) {
            val source = EventsPagingSource(apis)
            loadResult = source.load(
                PagingSource.LoadParams.Refresh(
                    key = page,
                    loadSize = pageSize,
                    placeholdersEnabled = false,
                )
            )
        }

        fun assertIsPage() {
            val r = loadResult
            when (r) {
                is PagingSource.LoadResult.Page -> assertTrue(true)
                is PagingSource.LoadResult.Error -> throw AssertionError(
                    "Expected Page, got Error: ${r.throwable}", r.throwable
                )
                else -> throw AssertionError("Expected Page, was $r")
            }
        }

        fun assertPageData(size: Int, prev: Int?, next: Int?, firstActor: String?) {
            val page = loadResult as PagingSource.LoadResult.Page<Int, RepoEvent>
            assertEquals(size, page.data.size)
            assertEquals(prev, page.prevKey)
            assertEquals(next, page.nextKey)
            if (firstActor != null) {
                assertEquals(firstActor, page.data.first().actor.login)
            }
        }

        fun enqueuePage(file: String, page: Int, withLink: Boolean) {
            val body = loadJson(file)
            val resp = MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(body)

            if (withLink) {
                resp.setHeader("Link", buildLinkHeaderFor(page))
            }
            server.enqueue(resp)
        }

        fun enqueue304(page: Int) {
            val resp = MockResponse()
                .setResponseCode(304)
                .setHeader("Content-Type", "application/json")
            server.enqueue(resp)
        }

        private fun buildLinkHeaderFor(page: Int): String {
            val parts = mutableListOf<String>()
            val base = "/events?per_page=30&page="

            if (page > 1) {
                parts += "<${server.url("$base${page - 1}")}>; rel=\"prev\""
            }
            parts += "<${server.url("${base}1")}>; rel=\"first\""
            parts += "<${server.url("$base${page + 1}")}>; rel=\"next\""
            parts += "<${server.url("${base}4")}>; rel=\"last\""
            return parts.joinToString(", ")
        }

        private fun loadJson(fileName: String): String {
            val stream = this::class.java.classLoader
                ?.getResourceAsStream(fileName)
                ?: error("Missing test resource: $fileName (place under src/test/resources)")
            return stream.bufferedReader().use { it.readText() }
        }
    }
}