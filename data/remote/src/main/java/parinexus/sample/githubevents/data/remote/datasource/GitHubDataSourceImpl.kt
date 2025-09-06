package parinexus.sample.githubevents.data.remote.datasource

import parinexus.sample.githubevents.data.remote.api.Apis
import androidx.paging.PagingSource
import parinexus.sample.githubevents.data.repository.datasource.GitHubDataSource
import parinexus.sample.githubevents.data.repository.model.RepoEvent

class GitHubDataSourceImpl(private val apis: Apis) : GitHubDataSource {
    override fun getEvents(): PagingSource<Int, RepoEvent> =
        EventsPagingSource(apis = apis)
}
