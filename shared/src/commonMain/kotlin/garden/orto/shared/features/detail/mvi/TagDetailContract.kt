package garden.orto.shared.features.detail.mvi

import garden.orto.shared.base.mvi.BasicUiState
import garden.orto.shared.base.mvi.UiEffect
import garden.orto.shared.base.mvi.UiEvent
import garden.orto.shared.base.mvi.UiState
import garden.orto.shared.domain.model.NoteState

interface TagDetailContract {
    sealed interface Event : UiEvent {
        data class OnGetNotes(val tagName: String) : Event
        data class DeleteNotes(val noteIds: List<Long>) : Event
        data class OnNoteClick(val idNote: Long) : Event
        object OnFABClick : Event

        object Retry : Event
    }

    data class State(
        val tagName: String,
        val notes: BasicUiState<List<NoteState>>
    ) : UiState

    sealed interface Effect : UiEffect {
        object NavigateToCreateNote : Effect
        data class NavigateToDetailNote(val idNote: Long) : Effect
        object NotesDeleted : Effect
    }
}