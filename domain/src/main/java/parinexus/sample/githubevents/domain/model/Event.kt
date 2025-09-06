package parinexus.sample.githubevents.domain.model

data class Event(
    val id: String,
    val actor: Actor,
    val repo: GithubRepository?,
    val createdAt: String
)
