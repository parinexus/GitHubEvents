package parinexus.sample.githubevents.data.local.mapper

import parinexus.sample.githubevents.data.local.entity.LocalEvent
import parinexus.sample.githubevents.data.local.entity.RemoteKeys
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoRemoteKeys

fun LocalEvent.toRepo(): RepoEvent = RepoEvent(
    id = id,
    type = type,
    actorLogin = actorLogin,
    actorAvatarUrl = actorAvatarUrl,
    repoName = repoName,
    repoUrl = repoUrl,
    createdAtEpochMillis = createdAtEpochMillis
)

fun RepoEvent.toLocal(): LocalEvent = LocalEvent(
    id = id,
    type = type,
    actorLogin = actorLogin,
    actorAvatarUrl = actorAvatarUrl,
    repoName = repoName.orEmpty(),
    repoUrl = repoUrl.orEmpty(),
    createdAtEpochMillis = createdAtEpochMillis
)

fun RemoteKeys.toRepo(): RepoRemoteKeys = RepoRemoteKeys(eventId, prevKey, nextKey)
fun RepoRemoteKeys.toLocal(): RemoteKeys = RemoteKeys(eventId, prevKey, nextKey)
