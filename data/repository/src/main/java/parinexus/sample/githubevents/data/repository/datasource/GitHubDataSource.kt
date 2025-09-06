package parinexus.sample.githubevents.data.repository.datasource

import androidx.paging.PagingSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent

interface GitHubDataSource {
    fun getEvents(): PagingSource<Int, RepoEvent>
}
