package garden.orto.shared.features.create.mvi

import garden.orto.shared.base.mvi.BasicUiState
import garden.orto.shared.base.mvi.UiEffect
import garden.orto.shared.base.mvi.UiEvent
import garden.orto.shared.base.mvi.UiState
import garden.orto.shared.domain.model.NoteState

interface NoteShareSheetContract {
    sealed interface Event : UiEvent {
        data class CreateNote(val content: String) : Event
        data class OnContentChanged(val content: String) : Event
        object OnBackPressed : Event
    }

    data class State(
        val note: BasicUiState<NoteState>
    ) : UiState

    sealed interface Effect : UiEffect {
        object NotesCreated : Effect
        data class NotesCreatedFailure(val message: String?) : Effect
        object BackNavigation : Effect
    }
}