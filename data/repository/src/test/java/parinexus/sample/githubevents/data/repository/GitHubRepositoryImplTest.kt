package parinexus.sample.githubevents.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.testing.ErrorRecovery
import androidx.paging.testing.LoadErrorHandler
import androidx.paging.testing.asSnapshot
import io.mockk.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import parinexus.sample.githubevents.data.repository.datasource.GitHubDataSource
import parinexus.sample.githubevents.data.repository.mapper.toDomainEvent
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.store.EventStore
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.libraries.test.BaseRobot
import parinexus.sample.githubevents.libraries.test.dsl.GIVEN
import parinexus.sample.githubevents.libraries.test.dsl.RUN_UNIT_TEST
import parinexus.sample.githubevents.libraries.test.dsl.THEN
import parinexus.sample.githubevents.libraries.test.dsl.WHEN
import parinexus.sample.githubevents.libraries.test.exception.TestException

class GitHubRepositoryImplTest {

    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher: TestDispatcher = StandardTestDispatcher(scheduler)
    private val robot = Robot(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher(scheduler))
        robot.setup()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_successful_getEvents() = RUN_UNIT_TEST(robot, testDispatcher) {
        GIVEN { mockSuccessfulGetEvents() }
        WHEN { collectFirstPageOfEvents() }
        THEN { checkEventsSuccessfulResult() }
    }

    @Test
    fun test_failure_getEvents() = RUN_UNIT_TEST(robot, testDispatcher) {
        GIVEN { mockFailureGetEvents() }
        WHEN { collectFirstPageOfEvents() }
        THEN { checkEventsFailureResult() }
    }

    @Test
    fun test_getEventById_passesThroughStore() = RUN_UNIT_TEST(robot, testDispatcher) {
        GIVEN { mockStoreHasEvent("42") }
        WHEN { callGetEventById("42") }
        THEN { checkGetEventByIdSuccessfulResult() }
    }

    @Test
    fun test_getEventById_returnsNullWhenMissing() = RUN_UNIT_TEST(robot, testDispatcher) {
        GIVEN { mockStoreMissingEvent("missing") }
        WHEN { callGetEventById("missing") }
        THEN { checkGetEventByIdNullResult() }
    }
}

private class Robot(
    private val testDispatcher: TestDispatcher
) : BaseRobot() {

    private val gitHubDataSource: GitHubDataSource = mockk()
    private val eventStore: EventStore = mockk(relaxUnitFun = true)

    private val gitHubRepository = GitHubRepositoryImpl(
        gitHubDataSource = gitHubDataSource,
        eventStore = eventStore
    )

    private var emittedEvents: List<Event> = emptyList()
    private var testExceptionThrown = false
    private var eventByIdResult: Event? = null

    override fun setup() {
        super.setup()
        clearMocks(gitHubDataSource, eventStore)
        emittedEvents = emptyList()
        eventByIdResult = null
        testExceptionThrown = false

        unmockkAll()
        mockAndroidLog()
    }

    fun mockSuccessfulGetEvents() {
        val dto1 = mockk<RepoEvent>()
        val dto2 = mockk<RepoEvent>()
        val pagingSource = SuccessPagingSource(listOf(dto1, dto2))
        every { gitHubDataSource.getEvents() } returns pagingSource

        mockkStatic("parinexus.sample.githubevents.data.repository.mapper.EventMapperKt")
        val e1 = mockk<Event>(relaxed = true)
        val e2 = mockk<Event>(relaxed = true)
        every { dto1.toDomainEvent() } returns e1
        every { dto2.toDomainEvent() } returns e2
    }

    fun mockFailureGetEvents() {
        every { gitHubDataSource.getEvents() } returns ThrowingPagingSource(TestException)
    }

    fun mockStoreHasEvent(id: String) {
        every { eventStore.get(id) } returns mockk()
    }

    fun mockStoreMissingEvent(id: String) {
        every { eventStore.get(id) } returns null
    }

    suspend fun collectFirstPageOfEvents() {
        try {
            emittedEvents = withContext(testDispatcher) {
                gitHubRepository.getEvents().asSnapshot(
                    onError = LoadErrorHandler { ErrorRecovery.THROW }
                )
            }
        } catch (_: TestException) {
            testExceptionThrown = true
        }
    }

    fun callGetEventById(id: String) {
        eventByIdResult = gitHubRepository.getEventById(id)
    }

    fun checkEventsSuccessfulResult() {
        assertTrue(emittedEvents.size == 2)
        verifyOrder {
            eventStore.put(emittedEvents[0])
            eventStore.put(emittedEvents[1])
        }
        confirmVerified(eventStore)
        assertTrue(!testExceptionThrown)
    }

    fun checkEventsFailureResult() {
        assertTrue(testExceptionThrown)
    }

    fun checkGetEventByIdSuccessfulResult() {
        assertTrue(eventByIdResult != null)
    }

    fun checkGetEventByIdNullResult() {
        assertTrue(eventByIdResult == null)
    }

    private fun mockAndroidLog() {
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any(), any()) } returns 0
    }

    private class SuccessPagingSource<T : Any>(
        private val items: List<T>
    ) : PagingSource<Int, T>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> =
            LoadResult.Page(data = items, prevKey = null, nextKey = null)

        override fun getRefreshKey(state: androidx.paging.PagingState<Int, T>): Int? = null
    }

    private class ThrowingPagingSource<T : Any>(
        private val error: Throwable
    ) : PagingSource<Int, T>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
            throw error
        }

        override fun getRefreshKey(state: androidx.paging.PagingState<Int, T>): Int? = null
    }
}