package parinexus.sample.githubevents.features.search.presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineScheduler
import androidx.paging.PagingData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.features.searchapi.usecase.SearchEventsUseCase
import parinexus.sample.githubevents.features.searchapi.usecase.StartEventsPollingUseCase
import parinexus.sample.githubevents.features.searchapi.usecase.StopEventsPollingUseCase
import parinexus.sample.githubevents.libraries.test.BaseRobot

class SearchScreenViewModelRobot(
    private val scheduler: TestCoroutineScheduler
) : BaseRobot(){

    // Mocks for the 3 use cases
    private val searchUseCase: SearchEventsUseCase = mockk(relaxed = true)
    private val startPolling: StartEventsPollingUseCase = mockk(relaxed = true)
    private val stopPolling: StopEventsPollingUseCase = mockk(relaxed = true)

    // Subject under test
    private lateinit var viewModel: SearchScreenViewModel

    // ---------- GIVEN ----------

    fun mockUseCaseWithEvents(count: Int = 0) {
        val flow: Flow<PagingData<UserEvent>> =
            if (count <= 0) flowOf(PagingData.empty())
            else {
                // We don't need real UserEvent instances; PagingData.from with mocks is fine.
                val items = List(count) { mockk<UserEvent>(relaxed = true) }
                flowOf(PagingData.from(items))
            }

        every { searchUseCase.invoke() } returns flow
    }

    // ---------- WHEN ----------

    fun createViewModel() {
        viewModel = SearchScreenViewModel(
            searchUseCase = searchUseCase,
            startPolling = startPolling,
            stopPolling = stopPolling
        )
    }

    /**
     * The ViewModel doesn’t have an 'immediate' flag; it always delegates to
     * startPolling(viewModelScope, 10_000L, 30). We keep the parameter for parity
     * with your sample DSL.
     */
    fun startAutoRefresh(immediate: Boolean) {
        viewModel.onScreenResumed()
    }

    fun stopAutoRefreshAndDrain() {
        viewModel.onScreenStopped()
        scheduler.advanceUntilIdle()
    }

    fun clearViewModelReflectively() {
        val m = viewModel.javaClass.getDeclaredMethod("onCleared")
        m.isAccessible = true
        m.invoke(viewModel)
        scheduler.advanceUntilIdle()
    }

    fun tick10s() {
        // Included to mirror your sample; the VM itself doesn't do time-based work.
        scheduler.advanceTimeBy(10_000L)
        scheduler.runCurrent()
    }

    // ---------- THEN ----------

    fun verifySearchUseCaseCalledExactly(times: Int) {
        verify(exactly = times) { searchUseCase.invoke() }
    }

    fun verifyStartPollingCalledExactly(times: Int) {
        verify(exactly = times) { startPolling.invoke(any<CoroutineScope>(), any(), any()) }
    }

    fun verifyStartPollingCalledWith(intervalMillis: Long, maxPages: Int) {
        val intervalSlot = slot<Long>()
        val pagesSlot = slot<Int>()
        verify { startPolling.invoke(any<CoroutineScope>(), capture(intervalSlot), capture(pagesSlot)) }
        assertEquals(intervalMillis, intervalSlot.captured)
        assertEquals(maxPages, pagesSlot.captured)
    }

    fun verifyStopPollingCalledExactly(times: Int) {
        verify(exactly = times) { stopPolling.invoke() }
    }

    fun assertUiHasPagingData() {
        val pd = runBlocking { viewModel.events.first() }
        assertNotNull("Expected PagingData emission from events Flow", pd)
    }
}