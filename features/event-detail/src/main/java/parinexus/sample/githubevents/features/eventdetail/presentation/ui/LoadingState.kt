package parinexus.sample.githubevents.features.eventdetail.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import parinexus.sample.githubevents.libraries.design.theme.GitHubEventsTheme
import parinexus.sample.githubevents.libraries.design.theme.PlaceHolderColor

@Composable
internal fun LoadingState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("loadingState"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(color = PlaceHolderColor)
        )
        Spacer(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(width = 100.dp, height = 24.dp)
                .background(
                    color = PlaceHolderColor,
                    shape = RoundedCornerShape(percent = 50)
                )
        )
        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(width = 150.dp, height = 24.dp)
                .background(
                    color = PlaceHolderColor,
                    shape = RoundedCornerShape(percent = 50)
                )
        )
        Spacer(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(width = 200.dp, height = 24.dp)
                .background(
                    color = PlaceHolderColor,
                    shape = RoundedCornerShape(percent = 50)
                )
        )
        Spacer(
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(208.dp)
                .background(
                    color = PlaceHolderColor,
                    shape = RoundedCornerShape(15.dp)
                )
        )
    }
}

@Preview
@Composable
fun LoadingStatePreview() {
    GitHubEventsTheme {
        Surface {
            LoadingState()
        }
    }
}
