package garden.orto.android.ui.features.detail.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import garden.orto.android.ui.components.NoteItem
import garden.orto.android.ui.components.state.ManagementResourceState
import garden.orto.android.ui.navigation.NavItem
import garden.orto.shared.domain.model.NoteState
import garden.orto.shared.features.detail.mvi.TagDetailContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoilApi
@Composable
fun TagDetailScreen(
    navController: NavController,
    onUiEvent: (TagDetailContract.Event) -> Unit,
    uiState: StateFlow<TagDetailContract.State>,
    uiEffect: Flow<TagDetailContract.Effect>,
    onNoteDetailNavigate: (Long) -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        uiEffect.collectLatest { effect ->
            when (effect) {
                is TagDetailContract.Effect.NavigateToDetailNote -> onNoteDetailNavigate(effect.idNote)
                TagDetailContract.Effect.NavigateToCreateNote -> navController.navigate(route = NavItem.NoteCreateNavItem.route)
                TagDetailContract.Effect.NotesDeleted -> scaffoldState.snackbarHostState.showSnackbar(
                    message = "Notes deleted"
                )
            }

        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onUiEvent(TagDetailContract.Event.OnFABClick)
                },
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add FAB",
//                tint = Color.White,
                )
            }
        },
        topBar = {
            ActionBar(
                name = state.tagName
            )
        }
    ) { padding ->
        ManagementResourceState(
            resourceState = state.notes,
            successView = { notes ->
                checkNotNull(notes)
                NotesList(
                    notes = notes,
                    onNoteClick = { idNote ->
                        onUiEvent(TagDetailContract.Event.OnNoteClick(idNote))
                    }
                )
            },
            modifier = Modifier.padding(padding),
            onTryAgain = { onUiEvent(TagDetailContract.Event.Retry) },
            onCheckAgain = { onUiEvent(TagDetailContract.Event.Retry) },
        )
    }
}

@ExperimentalCoilApi
@Composable
fun NotesList(
    notes: List<NoteState>,
    onNoteClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        items(notes) { state ->
            NoteItem(
                state = state,
                onClick = { onNoteClick(state.id) }
            )
        }
    }
}
