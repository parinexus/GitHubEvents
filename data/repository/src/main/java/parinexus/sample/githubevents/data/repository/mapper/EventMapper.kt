package parinexus.sample.githubevents.data.repository.mapper

import parinexus.sample.githubevents.data.repository.model.RepoActor
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoGithubRepository
import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.GithubRepository

fun RepoActor.toRepo(): Actor =
    Actor(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )

fun RepoGithubRepository.toRepo(): GithubRepository =
    GithubRepository(
        id = id,
        name = name,
        url = url
    )

fun RepoEvent.toDomainEvent(): Event =
    Event(
        id = id,
        actor = actor.toRepo(),
        repo = repo?.toRepo(),
        createdAt = createdAt
    )
