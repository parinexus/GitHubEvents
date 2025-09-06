package parinexus.sample.githubevents.data.remote.entity

import com.google.gson.annotations.SerializedName

data class GithubRepository(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)