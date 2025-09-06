package parinexus.sample.githubevents.data.repository.model

data class RepoEvent(
    val id: String,
    val actor: RepoActor,
    val repo: RepoGithubRepository?,
    val createdAt: String
)
