package parinexus.sample.githubevents.data.repository.datasource

import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys

interface RemoteKeysLocalDataSource {
    suspend fun insertAll(keys: List<RepoRemoteKeys>)
    suspend fun remoteKeysById(id: String): RepoRemoteKeys?
    suspend fun clear()
}