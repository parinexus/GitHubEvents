package parinexus.sample.githubevents.features.eventdetailapi.item

data class UserEvent(
    val id: String,
    val userActor: UserActor,
    val repo: UserGithubRepository?,
    val createdAt: String
)
