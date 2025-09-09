package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import parinexus.sample.githubevents.features.searchapi.model.UserEvent
import parinexus.sample.githubevents.libraries.design.theme.PlaceHolderColor
import parinexus.sample.githubevents.libraries.design.utils.formatAbsolute
import parinexus.sample.githubevents.libraries.design.utils.formatRelative
import parinexus.sample.githubevents.libraries.design.utils.humanRepoPath
import parinexus.sample.githubevents.libraries.design.utils.safeParseEpoch
import parinexus.sample.githubevents.libraries.design.utils.splitAbsolute
import parinexus.sample.githubevents.libraries.design.R as DesignResource


@Composable
fun EventItem(
    event: UserEvent,
    onClick: () -> Unit
) {
    val epoch: Long? = remember(event.createdAt) { safeParseEpoch(event.createdAt) }
    val absolute = remember(epoch) { epoch?.let { formatAbsolute(it) } ?: (event.createdAt ?: "") }
    val relative = remember(epoch) { epoch?.let { formatRelative(it) } ?: "" }
    val (dateText, timeText) = remember(absolute) { splitAbsolute(absolute) }

    val repoName = event.repo?.name.orEmpty()
    val repoUrl  = event.repo?.url
    val repoPath = remember(repoUrl) { repoUrl?.let { humanRepoPath(it) }.orEmpty() }

    Surface(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(event.userActor.avatarUrl.ifBlank { DesignResource.drawable.ic_github })
                    .crossfade(true)
                    .build(),
                contentDescription = "user avatar",
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(PlaceHolderColor),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = event.userActor.login,
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.SemiBold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (relative.isNotEmpty()) {
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "• $relative",
                            style = MaterialTheme.typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }
                }

                if (repoName.isNotEmpty() || repoPath.isNotEmpty()) {
                    val repoText = buildAnnotatedString {
                        if (repoName.isNotEmpty()) {
                            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) { append(repoName) }
                        }
                        if (repoPath.isNotEmpty()) {
                            if (repoName.isNotEmpty()) append("  ")
                            withStyle(SpanStyle(color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f))) {
                                append(repoPath)
                            }
                        }
                    }
                    Text(
                        text = repoText,
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                if (dateText.isNotEmpty() || timeText.isNotEmpty()) {
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        if (dateText.isNotEmpty()) {
                            Text(
                                text = dateText,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (dateText.isNotEmpty() && timeText.isNotEmpty()) {
                            Text(
                                text = "  •  ",
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        if (timeText.isNotEmpty()) {
                            Text(
                                text = timeText,
                                style = MaterialTheme.typography.caption,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}