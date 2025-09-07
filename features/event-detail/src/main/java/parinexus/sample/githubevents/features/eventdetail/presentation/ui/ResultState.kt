package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import parinexus.sample.githubevents.features.eventdetailapi.item.UserEvent
import parinexus.sample.githubevents.libraries.design.theme.HintColor

@Composable
internal fun ResultState(
    eventDetail: UserEvent,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("resultState"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colors.onSecondary),
            painter = rememberAsyncImagePainter(model = eventDetail.userActor.avatarUrl),
            contentDescription = "avatarImage"
        )
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = eventDetail.userActor.login,
            fontSize = 23.sp,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground
            )
        )
        eventDetail.repo?.name?.let {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                fontSize = 20.sp,
                text = it,
                color = HintColor
            )
        }
    }
}