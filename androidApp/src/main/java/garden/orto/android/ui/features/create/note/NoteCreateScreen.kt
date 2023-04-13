package garden.orto.android.ui.features.create.note

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import garden.orto.android.OrtoTheme
import garden.orto.android.ui.components.state.ManagementResourceState
import garden.orto.shared.features.create.mvi.NoteShareSheetContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NoteCreateScreen(
    navController: NavController,
    onUiEvent: (NoteShareSheetContract.Event) -> Unit,
    uiState: StateFlow<NoteShareSheetContract.State>,
    uiEffect: Flow<NoteShareSheetContract.Effect>,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    var content by remember { mutableStateOf("") }
    val state by uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        uiEffect.collectLatest { effect ->
            when (effect) {
                NoteShareSheetContract.Effect.NotesCreated -> navController.popBackStack()
                NoteShareSheetContract.Effect.BackNavigation -> navController.popBackStack()
                is NoteShareSheetContract.Effect.NotesCreatedFailure -> effect.message?.let {
                    scaffoldState.snackbarHostState.showSnackbar(it)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ActionBar(
                onBackPressed = { onUiEvent(NoteShareSheetContract.Event.OnBackPressed) },
                actionSaveNote = {
                    onUiEvent(NoteShareSheetContract.Event.OnContentChanged(content))
                    onUiEvent(NoteShareSheetContract.Event.CreateNote(content))
                }
            )
        }
    ) { padding ->
        ManagementResourceState(
            resourceState = state.note,
            successView = { noteState ->
                checkNotNull(noteState)
                NoteEditor(
                    content = content,
                    onContentChange = { input ->
                        content = input
                    }
                )
            },
            modifier = Modifier.padding(padding),
            onTryAgain = { },
            onCheckAgain = { },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditor(
    content: String,
    onContentChange: (String) -> Unit,
) {
    TextField(
        placeholder = { Text("Write your thoughts here") },
        value = content,
        onValueChange = { newText ->
            onContentChange(newText)
        }
    )
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun PreNoteEditor() {
    OrtoTheme {
        val onContentChange: (String) -> Unit = {}
        val state = "# content\n\nWith also some #tags"
        NoteEditor(state, onContentChange)
    }
}