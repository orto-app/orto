package garden.orto.shared.features.create.mvi

import garden.orto.shared.base.mvi.UiEffect
import garden.orto.shared.base.mvi.UiEvent
import garden.orto.shared.domain.model.NoteState

interface NoteShareSheetContract {
    sealed interface Event : UiEvent {
        data class CreateNotes(val notes: List<NoteState>) : Event
    }

    sealed interface Effect : UiEffect {
        object NotesCreated : Effect
    }
}