package parinexus.sample.githubevents

import parinexus.sample.githubevents.features.search.presentation.SearchScreen
import parinexus.sample.githubevents.features.search.presentation.SearchScreenViewModel
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreen
import parinexus.sample.githubevents.features.eventdetail.presentation.EventDetailScreenViewModel
import parinexus.sample.githubevents.libraries.navigation.BuildConfig
import parinexus.sample.githubevents.libraries.navigation.DestinationArgs
import parinexus.sample.githubevents.libraries.navigation.Destinations
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.core.net.toUri

@Composable
fun Navigator(navHostController: NavHostController) {
    val actions = remember { Actions(navHostController) }
    NavHost(navController = navHostController, startDestination = Destinations.SEARCH) {
        composable(route = Destinations.SEARCH) { entry ->
            val viewModel: SearchScreenViewModel = hiltViewModel(entry)
            SearchScreen(
                searchScreenViewModel = viewModel,
                navigateToEventDetail = actions.openEventDetailScreen,
                screenLifecycle = entry.lifecycle
            )
        }
        composable(
            route = "${Destinations.EVENT_DETAIL}/{${DestinationArgs.EVENT_ID}}",
            arguments = listOf(navArgument(DestinationArgs.EVENT_ID) { type = NavType.StringType })
        ) {
            val viewModel: EventDetailScreenViewModel = hiltViewModel()
            val context = LocalContext.current
            EventDetailScreen(
                eventDetailScreenViewModel = viewModel,
                openLink = {
                    try {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW).apply { data = it.toUri() }
                        )
                    } catch (e: ActivityNotFoundException) {
                        if (BuildConfig.DEBUG)
                            e.printStackTrace()
                    }
                },
                navigateBack = actions.navigateBack
            )
        }
    }
}

class Actions(private val navHostController: NavHostController) {

    val openEventDetailScreen: (username: String) -> Unit = {
        navHostController.navigate("${Destinations.EVENT_DETAIL}/$it")
    }

    val navigateBack: () -> Unit = { navHostController.popBackStack() }
}
