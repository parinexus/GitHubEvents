package parinexus.sample.githubevents.data.local.datasource

import parinexus.sample.githubevents.data.local.dao.RemoteKeysDao
import parinexus.sample.githubevents.data.local.mapper.toLocal
import parinexus.sample.githubevents.data.local.mapper.toRepo
import parinexus.sample.githubevents.data.repository.datasource.RemoteKeysLocalDataSource
import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys
import javax.inject.Inject

class RemoteKeysLocalDataSourceImpl @Inject constructor(
    private val dao: RemoteKeysDao
) : RemoteKeysLocalDataSource {
    override suspend fun insertAll(keys: List<RepoRemoteKeys>) =
        dao.insertAll(keys.map { it.toLocal() })

    override suspend fun remoteKeysById(id: String): RepoRemoteKeys? =
        dao.remoteKeysById(id)?.toRepo()

    override suspend fun clear() = dao.clear()
}