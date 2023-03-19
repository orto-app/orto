package coop.uwutech.orto.android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.Tag
import coop.uwutech.orto.shared.cache.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

private const val DEFAULT_TIMEOUT = 5000L

@OptIn(ExperimentalCoroutinesApi::class)
class NotesForTagViewModel(
    private val noteRepository: NoteRepository, private val tagRepository: TagRepository
) : ViewModel() {

    private val notes: Flow<List<Note>> = noteRepository.allNotes

    private val tags: Flow<List<List<Tag>>> = notes.flatMapLatest { noteList ->
        // Create a Flow of List<Tag> for each Note
        val tagFlowList = noteList.map { note ->
            tagRepository.getTags(note)
        }
        // Combine all the List<Tag> flows into one Flow<List<List<Tag>>>
        combine(tagFlowList) { tagListArray -> tagListArray.toList() }
    }

    // The UI collects from this StateFlow to get its state updates
    val uiState: StateFlow<List<Result>> = tags.zip(notes) { t, n ->
        val tagsNames = t.map { tagList -> tagList.map { tag -> tag.name } }
        n.mapIndexed { index, note ->
            Result.NoteCardUiState(note=note, tagsNames=tagsNames[index])
        }
    } .stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
    )
}

// Represents different states for the NotesForTag screen
sealed interface Result {
    object Loading : Result
    data class NoteCardUiState(val note: Note, val tagsNames: List<String>) : Result {
        val title = note.title
        val url = note.url
        val image = note.image
        val content = note.content
        val tags = tagsNames
    }

    data class Error(val exception: Throwable) : Result
}
