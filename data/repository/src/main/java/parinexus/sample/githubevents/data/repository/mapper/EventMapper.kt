package parinexus.sample.githubevents.data.repository.mapper

import parinexus.sample.githubevents.data.repository.model.RepoEvent
import parinexus.sample.githubevents.domain.model.Actor
import parinexus.sample.githubevents.domain.model.Event
import parinexus.sample.githubevents.domain.model.RepoModel
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

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
        createdAt = epochToLocalText(createdAtEpochMillis)
    )

fun epochToLocalText(epochMillis: Long, zone: ZoneId = ZoneId.systemDefault()): String {
    val zdt = Instant.ofEpochMilli(epochMillis).atZone(zone)
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())
        .format(zdt)
}
