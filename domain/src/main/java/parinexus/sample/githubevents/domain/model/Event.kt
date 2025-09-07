package parinexus.sample.githubevents.domain.model

data class Event(
    val id: String,
    val type: String,
    val actor: Actor,
    val repo: RepoModel?,
    val createdAt: String
)
