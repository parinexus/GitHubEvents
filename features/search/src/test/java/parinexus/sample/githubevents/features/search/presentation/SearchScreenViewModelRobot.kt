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

    private val searchUseCase: SearchEventsUseCase = mockk(relaxed = true)
    private val startPolling: StartEventsPollingUseCase = mockk(relaxed = true)
    private val stopPolling: StopEventsPollingUseCase = mockk(relaxed = true)

    private lateinit var viewModel: SearchScreenViewModel

    fun mockUseCaseWithEvents(count: Int = 0) {
        val flow: Flow<PagingData<UserEvent>> =
            if (count <= 0) flowOf(PagingData.empty())
            else {
                val items = List(count) { mockk<UserEvent>(relaxed = true) }
                flowOf(PagingData.from(items))
            }

        every { searchUseCase.invoke() } returns flow
    }

    fun createViewModel() {
        viewModel = SearchScreenViewModel(
            searchUseCase = searchUseCase,
            startPolling = startPolling,
            stopPolling = stopPolling
        )
    }

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

    fun verifySearchUseCaseCalledExactly(times: Int) {
        verify(exactly = times) { searchUseCase.invoke() }
    }

    fun verifyStartPollingCalledExactly(times: Int) {
        verify(exactly = times) { startPolling.invoke(any<CoroutineScope>(), any()) }
    }

    fun verifyStartPollingCalledWith(intervalMillis: Long, maxPages: Int) {
        val intervalSlot = slot<Long>()
        val pagesSlot = slot<Int>()
        verify {
            startPolling.invoke(
                any<CoroutineScope>(),
                any()
            )
        }
        assertEquals(intervalMillis, intervalSlot.captured)
        assertEquals(maxPages, pagesSlot.captured)
    }

    fun verifyStopPollingCalledExactly(times: Int) {
        verify(exactly = times) { stopPolling.invoke() }
    }

    fun assertUiHasPagingData() {
        val pd = runBlocking { viewModel.uiState.first() }
        assertNotNull("Expected PagingData emission from events Flow", pd)
    }
}