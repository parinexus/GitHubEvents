package parinexus.sample.githubevents.features.search.presentation.ui

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import parinexus.sample.githubevents.feature.search.R
import parinexus.sample.githubevents.libraries.design.theme.GitHubEventsTheme
import parinexus.sample.githubevents.libraries.design.theme.HintColor
import parinexus.sample.githubevents.libraries.design.R as DesignResource

@Composable
internal fun InitialState(
    modifier: Modifier = Modifier,
    onStartSearch: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = DesignResource.drawable.ic_github),
            contentDescription = "initialStatePlaceHolderImage",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.surface)
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.startSearch), color = HintColor)
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Rounded.ArrowForward,
                tint = HintColor,
                contentDescription = "arrowIcon"
            )
            FloatingActionButton(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .testTag("searchButton"),
                shape = RoundedCornerShape(10.dp),
                contentColor = MaterialTheme.colors.onBackground,
                onClick = { onStartSearch() }
            ) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "searchIcon")
            }
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun InitialStatePreview() {
    GitHubEventsTheme { Surface { InitialState(onStartSearch = {}) } }
}