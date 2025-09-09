package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Source
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.libraries.design.theme.HintColor
import parinexus.sample.githubevents.libraries.design.utils.formatAbsolute
import parinexus.sample.githubevents.libraries.design.utils.formatRelative
import parinexus.sample.githubevents.libraries.design.utils.humanRepoPath
import parinexus.sample.githubevents.libraries.design.utils.safeParseEpoch
import parinexus.sample.githubevents.libraries.design.utils.splitAbsolute

@Composable
internal fun ResultState(
    eventDetail: UserEvent,
    openLink: (String) -> Unit,
) {
    val avatar = eventDetail.userActor.avatarUrl
    val login  = eventDetail.userActor.login
    val repoName = eventDetail.repo?.name.orEmpty()
    val repoUrl  = eventDetail.repo?.url
    val repoPath = remember(repoUrl) { repoUrl?.let(::humanRepoPath).orEmpty() }

    val epoch = remember(eventDetail) { eventDetail.createdAt?.let(::safeParseEpoch) }
    val relative = remember(epoch) { epoch?.let { formatRelative(it) } ?: "" }
    val absolute = remember(epoch, eventDetail) { epoch?.let { formatAbsolute(it) } ?: (eventDetail.createdAt ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("resultState")
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Image(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.onSecondary),
            painter = rememberAsyncImagePainter(model = avatar),
            contentDescription = "avatarImage"
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = login,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(6.dp))
        if (relative.isNotEmpty()) {
            Text(
                text = relative,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        if (absolute.isNotEmpty()) {
            Text(
                text = absolute,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }

        Spacer(Modifier.height(16.dp))
        Card(
            elevation = 2.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = MaterialTheme.colors.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                if (repoName.isNotEmpty() || repoPath.isNotEmpty()) {
                    MetaRow(
                        icon = Icons.Default.Source,
                        label = "Repository",
                        value = repoName.ifEmpty { repoPath }
                    )
                    if (!repoUrl.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        LinkChip(
                            text = "Open repo",
                            onClick = { openLink(repoUrl) }
                        )
                    }
                    Divider(Modifier.padding(vertical = 12.dp))
                }

                MetaRow(
                    icon = Icons.Default.Person,
                    label = "Actor",
                    value = login
                )
                Spacer(Modifier.height(8.dp))
                LinkChip(
                    text = "Open profile",
                    onClick = { openLink("https://github.com/$login") }
                )

                if (absolute.isNotEmpty()) {
                    Divider(Modifier.padding(vertical = 12.dp))

                    val (dateText, timeText) = remember(absolute) { splitAbsolute(absolute) }

                    MetaRow(
                        icon = Icons.Default.CalendarToday,
                        label = "Date",
                        value = dateText
                    )
                    Spacer(Modifier.height(8.dp))
                    MetaRow(
                        icon = Icons.Default.Schedule,
                        label = "Time",
                        value = timeText
                    )
                }

            }
        }

        if (!repoUrl.isNullOrBlank()) {
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { openLink(repoUrl) },
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.OpenInNew, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Open in GitHub")
            }
        }

        if (repoName.isNotEmpty()) {
            Spacer(Modifier.height(14.dp))
            Text(
                text = repoName,
                style = MaterialTheme.typography.subtitle1,
                color = HintColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun MetaRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colors.onSurface.copy(alpha = 0.75f))
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f),
            modifier = Modifier
                .widthIn(min = 64.dp)
                .weight(0.35f, fill = false)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LinkChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = MaterialTheme.colors.primary)
            Spacer(Modifier.width(6.dp))
            Text(text = text, color = MaterialTheme.colors.primary, style = MaterialTheme.typography.caption)
        }
    }
}