package garden.orto.android.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import garden.orto.android.ui.features.create.note.NoteCreateScreen
import garden.orto.android.ui.features.detail.tag.TagDetailScreen
import garden.orto.shared.features.create.mvi.NoteShareSheetViewModel
import garden.orto.shared.features.detail.mvi.TagDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@Composable
fun Navigation(
    vmTagDetail: TagDetailViewModel,
    vmNoteShareSheet: NoteShareSheetViewModel,
    uiHomeTagName: String
) {
    val tagDetailNavItem = object : NavItem.TagDetailNavItem(uiHomeTagName) {}

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = tagDetailNavItem.route
    ) {
        composable(tagDetailNavItem) { backStackEntry ->
            TagDetailScreen(
                navController = navController,
                onUiEvent = { event -> vmTagDetail.setEvent(event) },
                uiState = vmTagDetail.uiState,
                uiEffect = vmTagDetail.effect,
                onNoteDetailNavigate = { idNote ->
                    // TODO
//                    navController.navigate()
//                    vmNoteDetail.setEvent()
                }
            )
        }
        composable(NavItem.NoteCreateNavItem) { backStackEntry ->
            NoteCreateScreen(
                navController = navController,
                onUiEvent = { event -> vmNoteShareSheet.setEvent(event) },
                uiState = vmNoteShareSheet.uiState,
                uiEffect = vmNoteShareSheet.effect,
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

private inline fun <reified T> NavBackStackEntry.findArg(key: String): T {
    val value = arguments?.get(key)
    requireNotNull(value)
    return value as T
}