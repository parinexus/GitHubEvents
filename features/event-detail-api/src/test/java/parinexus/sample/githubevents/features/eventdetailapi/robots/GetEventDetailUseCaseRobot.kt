package parinexus.sample.githubevents.features.eventdetailapi.robots

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.TestCoroutineScheduler
import parinexus.sample.githubevents.domain.interactor.GetEventByIdInteractor
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.features.eventdetailapi.mapper.toDetailsView
import parinexus.sample.githubevents.features.eventdetailapi.usecase.DomainEvent
import parinexus.sample.githubevents.features.eventdetailapi.usecase.GetEventDetailUseCase
import parinexus.sample.githubevents.libraries.test.BaseRobot
import kotlin.test.assertEquals
import kotlin.test.assertNull

private const val MAPPER_FILE =
    "parinexus.sample.githubevents.features.eventdetailapi.mapper.EventDetailItemMapperKt"

class GetEventDetailUseCaseRobot(
    @Suppress("unused") private val scheduler: TestCoroutineScheduler
) : BaseRobot(){
    private val interactor: GetEventByIdInteractor = mockk()
    private val useCase = GetEventDetailUseCase(interactor)

    private lateinit var domainEvent: DomainEvent
    private lateinit var mappedUserEvent: UserEvent
    private var actual: UserEvent? = null

    fun mockInteractorReturnsEventFor(id: String) = apply {
        domainEvent = mockk(relaxed = true)
        coEvery { interactor.invoke(id) } returns domainEvent
    }

    fun mockInteractorReturnsNullFor(id: String) = apply {
        coEvery { interactor.invoke(id) } returns null
    }

    fun mockMapperToReturnUserEvent() = apply {
        mappedUserEvent = mockk(relaxed = true)
        mockkStatic(MAPPER_FILE)

        every { any<DomainEvent>().toDetailsView() } returns mappedUserEvent
    }

    suspend fun invokeUseCase(id: String) = apply {
        actual = useCase(id)
    }

    fun assertResultIsMapped() = apply {
        assertEquals(mappedUserEvent, actual)
    }

    fun assertResultIsNull() = apply {
        assertNull(actual)
    }

    fun verifyInteractorCalledExactly(times: Int) = apply {
        coVerify(exactly = times) { interactor.invoke(any()) }
        confirmVerified(interactor)
    }

    fun verifyInteractorCalledWith(id: String) = apply {
        coVerify { interactor.invoke(id) }
    }
}