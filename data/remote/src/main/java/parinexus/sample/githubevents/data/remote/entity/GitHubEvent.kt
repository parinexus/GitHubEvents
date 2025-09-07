package parinexus.sample.githubevents.data.remote.entity

import com.google.gson.annotations.SerializedName

data class GitHubEvent(
    val id: String,
    @SerializedName( "type") val type: EventType,
    val actor: Actor,
    val githubRepository: GithubRepository?,
    @SerializedName( "created_at") val createdAt: String
)