package coop.uwutech.orto.shared.features.detail.mvi

import coop.uwutech.orto.shared.base.mvi.BasicUiState
import coop.uwutech.orto.shared.base.mvi.UiEvent
import coop.uwutech.orto.shared.base.mvi.UiState
import coop.uwutech.orto.shared.domain.model.NoteItemState

interface TagDetailContract {
    sealed interface Event : UiEvent {
        data class OnGetNotes(val tagName: String) : Event
        object Retry : Event
    }

    data class State(
        val tagName: String,
        val notes: BasicUiState<List<NoteItemState>>
    ) : UiState
}