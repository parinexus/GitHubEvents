package parinexus.sample.githubevents.data.repository.model

data class RepoEvent(
    val id: String,
    val type: String,
    val actorLogin: String,
    val actorAvatarUrl: String,
    val repoName: String?,
    val repoUrl: String?,
    val createdAtEpochMillis: Long
)

data class RepoRemoteKeys(
    val eventId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
