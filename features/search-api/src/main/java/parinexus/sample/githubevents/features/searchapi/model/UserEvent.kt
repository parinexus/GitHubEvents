package parinexus.sample.githubevents.features.searchapi.model

data class UserEvent(
    val id: String,
    val userActor: UserActor,
    val repo: UserGithubRepository?,
    val createdAt: String
)
