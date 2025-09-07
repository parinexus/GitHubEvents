package parinexus.sample.githubevents.data.remote.mapper

import java.time.Instant
import parinexus.sample.githubevents.data.remote.entity.GitHubEvent
import parinexus.sample.githubevents.data.repository.model.RepoEvent

fun GitHubEvent.toRepo(): RepoEvent = RepoEvent(
    id = id,
    type = type.name,
    actorLogin = actor.login,
    actorAvatarUrl = actor.avatarUrl,
    repoName = githubRepository?.name,
    repoUrl = githubRepository?.url,
    createdAtEpochMillis = runCatching { Instant.parse(createdAt).toEpochMilli() }.getOrDefault(0L)
)
