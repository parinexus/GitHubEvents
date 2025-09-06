package parinexus.sample.githubevents.data.remote.entity

import com.google.gson.annotations.SerializedName

data class Actor(
    val id: Long,
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String
)