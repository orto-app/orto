package coop.uwutech.orto.android.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun OrtoNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "profile"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("NotesForTag") {
            NotesForTagScreen(
                onNavigateToFriends = { navController.navigate("friendsList") },
                /*...*/
            )
        }
        composable("notedetail") { NoteDetail(/*...*/) }
    }
}
