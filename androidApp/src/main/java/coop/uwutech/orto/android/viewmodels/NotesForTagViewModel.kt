package coop.uwutech.orto.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.TagRepository
import kotlinx.coroutines.flow.*

private const val DEFAULT_TIMEOUT = 5000L

class NotesForTagViewModel(
    noteRepository: NoteRepository,
    tagRepository: TagRepository
) : ViewModel() {

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<NotesForTagUiState> = noteRepository.allNotes
        .map {
            // Update View with the latest favorite news
            // Writes to the value property of MutableStateFlow,
            // adding a new element to the flow and updating all
            // of its collectors
            NotesForTagUiState.NoteCardUiState(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = NotesForTagUiState.Loading,
            started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
        )
}

// Represents different states for the LatestNews screen
sealed interface NotesForTagUiState {
    object Loading : NotesForTagUiState
    data class NoteCardUiState(val note: Note) : NotesForTagUiState {
        val title = note.title
        val url = note.url
        val image = note.image
        val content = note.content
    }
    data class Error(val exception: Throwable) : NotesForTagUiState
}
