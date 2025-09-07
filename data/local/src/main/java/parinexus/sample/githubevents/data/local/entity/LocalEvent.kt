package parinexus.sample.githubevents.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "events",
    indices = [Index(value = ["actorLogin", "actorAvatarUrl"], unique = true)]
)
data class LocalEvent(
    @PrimaryKey val id: String,
    val type: String,
    val actorLogin: String,
    val actorAvatarUrl: String,
    val repoName: String?,
    val repoUrl: String?,
    val createdAtEpochMillis: Long
)