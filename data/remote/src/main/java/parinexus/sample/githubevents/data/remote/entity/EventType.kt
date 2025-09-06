package parinexus.sample.githubevents.data.remote.entity

import com.google.gson.annotations.SerializedName

enum class EventType {
    @SerializedName("PushEvent")
    PushEvent,
    @SerializedName("PullRequestEvent")
    PullRequestEvent,
    @SerializedName("IssuesEvent")
    IssuesEvent,
    @SerializedName("ForkEvent")
    ForkEvent,
    @SerializedName("ReleaseEvent")
    ReleaseEvent,
    Other;

    companion object {
        val subscribedTypes = setOf(
            PushEvent,
            PullRequestEvent,
            IssuesEvent,
            ForkEvent,
            ReleaseEvent
        )

        fun from(raw: String): EventType = values().firstOrNull {
            it.name == raw
        } ?: Other
    }
}