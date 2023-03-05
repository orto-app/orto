package coop.uwutech.orto.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.TagRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesForTagViewModel(
    noteRepository: NoteRepository,
    tagRepository: TagRepository
) : ViewModel() {

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(NotesForTagUiState.Success(emptyList())
    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<LatestNewsUiState> = StateFlow(NotesForTagUiState.Success(emptyList()))

    init {
        viewModelScope.launch {
            noteRepository.getAllNotes()
                // Update View with the latest favorite news
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect { favoriteNews ->
                    uiState.value = NotesForTagUiState.Success(favoriteNews)
                }
        }
    }
}

// Represents different states for the LatestNews screen
sealed class NotesForTagUiState {
    data class Success(val notes: List<ArticleHeadline>): NotesForTagUiState()
    data class Error(val exception: Throwable): NotesForTagUiState()
}
