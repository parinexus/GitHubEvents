package parinexus.sample.githubevents.data.repository.mapper

import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.RepoModel

fun toRepoActor(actorLogin: String, actorAvatarUrl: String): Actor =
    Actor(
        login = actorLogin,
        avatarUrl = actorAvatarUrl
    )

fun toRepoModel(repoName: String?, repoUrl: String?): RepoModel =
    RepoModel(
        name = repoName,
        url = repoUrl
    )

fun RepoEvent.toDomainEvent(): Event =
    Event(
        id = id,
        type = type,
        actor = toRepoActor(actorLogin,actorAvatarUrl),
        repo = toRepoModel(repoName, repoUrl),
        createdAt = createdAtEpochMillis.toString()
    )