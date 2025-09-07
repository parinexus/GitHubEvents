package parinexus.sample.githubevents.features.search.presentation.ui

import parinexus.sample.githubevents.libraries.design.theme.GitHubEventsTheme
import parinexus.sample.githubevents.libraries.design.theme.PlaceHolderColor
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun EventPlaceHolder() {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(48.dp)
                .background(color = PlaceHolderColor, shape = CircleShape)
        )
        Spacer(
            modifier = Modifier
                .padding(start = 16.dp)
                .fillMaxWidth()
                .height(16.dp)
                .background(color = PlaceHolderColor, shape = RoundedCornerShape(8.dp))
        )
    }
}

@Preview(widthDp = 300, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun EventPalaceHolderPreview() {
    GitHubEventsTheme {
        Surface {
            EventPlaceHolder()
        }
    }
}
