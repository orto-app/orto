package garden.orto.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import garden.orto.android.ui.features.TagDetailScreen
import garden.orto.shared.features.detail.mvi.TagDetailContract
import garden.orto.shared.features.detail.mvi.TagDetailViewModel

@ExperimentalCoilApi
@Composable
fun Navigation(
    vmTagDetail: TagDetailViewModel,
    uiHomeTagName: String
) {
    val tagDetailNavItem = object : NavItem.TagDetailNavItem(uiHomeTagName) {}
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = tagDetailNavItem.route
    ) {
        composable(tagDetailNavItem) { backStackEntry ->
            val tagName = backStackEntry.arguments?.getString("tagName")!!
            vmTagDetail.setEvent(TagDetailContract.Event.OnGetNotes(tagName))
            TagDetailScreen(
                onNoteClick = { },
                viewModel = vmTagDetail
            )
        }
    }
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args
    ) {
        content(it)
    }
}
