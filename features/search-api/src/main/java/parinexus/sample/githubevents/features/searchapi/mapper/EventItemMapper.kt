package parinexus.sample.githubevents.features.searchapi.mapper

import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.RepoModel
import parinexus.sample.githubevents.features.searchapi.model.UserActor
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.features.searchapi.model.UserGithubRepository

fun Actor.toUserActor(): UserActor =
    UserActor(
        login = login,
        avatarUrl = avatarUrl
    )

fun RepoModel.toUserGithubRepository(): UserGithubRepository =
    UserGithubRepository(
        name = name,
        url = url
    )

fun Event.toView(): UserEvent =
    UserEvent(
        id = id,
        userActor = actor.toUserActor(),
        repo = repo?.toUserGithubRepository() ,
        createdAt = createdAt
    )
