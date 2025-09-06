package parinexus.sample.githubevents.data.remote.mapper

import parinexus.sample.githubevents.data.remote.entity.Actor
import parinexus.sample.githubevents.data.remote.entity.GitHubEvent
import parinexus.sample.githubevents.data.remote.entity.GithubRepository
import parinexus.sample.githubevents.data.repository.model.RepoActor
import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.data.repository.model.RepoGithubRepository

fun Actor.toRepo(): RepoActor =
    RepoActor(
        id = id,
        login = login,
        avatarUrl = avatarUrl
    )

fun GithubRepository.toRepo(): RepoGithubRepository =
    RepoGithubRepository(
        id = id,
        name = name,
        url = url
    )

fun GitHubEvent.toRepo(): RepoEvent =
    RepoEvent(
        id = id,
        actor = actor.toRepo(),
        repo = githubRepository?.let { it.toRepo() },
        createdAt = createdAt
    )
