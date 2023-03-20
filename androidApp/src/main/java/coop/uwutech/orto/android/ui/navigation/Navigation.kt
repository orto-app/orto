package coop.uwutech.orto.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coop.uwutech.orto.android.ui.features.TagDetailScreen
import coop.uwutech.orto.shared.features.detail.mvi.TagDetailViewModel

@ExperimentalCoilApi
@Composable
fun Navigation(
    vmTagDetail: TagDetailViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // FIXME: HARDCODED
//        startDestination = NavItem.TagDetail.route
        startDestination = NavItem.TagDetail.createNavRoute("home")
    ) {
        composable(NavItem.TagDetail) {
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
