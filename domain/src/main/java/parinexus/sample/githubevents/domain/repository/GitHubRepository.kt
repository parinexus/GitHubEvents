package parinexus.sample.githubevents.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import parinexus.sample.githubevents.domain.model.Event

interface GitHubRepository {

    fun getEvents(): Flow<PagingData<Event>>

    fun getEventById(id: String): Event?
}
