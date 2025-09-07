package parinexus.sample.githubevents.features.eventdetailapi.mapper

import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.RepoModel
import parinexus.sample.githubevents.features.eventdetailapi.item.UserActor
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.features.eventdetailapi.item.UserGithubRepository

fun Actor.toDetailActor(): UserActor =
    UserActor(
        login = login,
        avatarUrl = avatarUrl
    )

fun RepoModel.toDetailGithubRepository(): UserGithubRepository =
    UserGithubRepository(
        name = name,
        url = url
    )

fun Event.toDetailsView(): UserEvent =
    UserEvent(
        id = id,
        userActor = actor.toDetailActor(),
        repo = repo?.toDetailGithubRepository() ,
        createdAt = createdAt
    )
