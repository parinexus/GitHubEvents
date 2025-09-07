package parinexus.sample.githubevents.data.repository.datasource

import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys
import kotlin.collections.forEach

class FakeRemoteKeysLocalDataSource : RemoteKeysLocalDataSource {
    private val map = mutableMapOf<String, RepoRemoteKeys>()
    override suspend fun insertAll(keys: List<RepoRemoteKeys>) { keys.forEach { map[it.eventId] = it } }
    override suspend fun remoteKeysById(id: String): RepoRemoteKeys? = map[id]
    override suspend fun clear() = map.clear()
}
