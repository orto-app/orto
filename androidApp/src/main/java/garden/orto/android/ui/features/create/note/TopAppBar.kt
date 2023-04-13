package garden.orto.android.ui.features.create.note

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import garden.orto.android.ui.components.ActionBarIcon
import garden.orto.android.ui.components.ArrowBackIcon

@Composable
fun ActionBar(
    onBackPressed: () -> Unit,
    actionSaveNote: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "New note") },
        navigationIcon = { ArrowBackIcon(onBackPressed) },
        actions = {
            ActionBarIcon(
                onClick = actionSaveNote,
                icon = Icons.Filled.Done
            )
        }
    )
}