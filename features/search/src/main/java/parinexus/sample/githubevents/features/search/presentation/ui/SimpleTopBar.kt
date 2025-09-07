package parinexus.sample.githubevents.features.search.presentation.ui

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
internal fun SimpleTopBar() {
    TopAppBar(
        title = { Text(text = "GitHub Events") }
    )
}
