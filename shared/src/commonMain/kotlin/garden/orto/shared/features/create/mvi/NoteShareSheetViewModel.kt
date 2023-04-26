package garden.orto.shared.features.create.mvi

import garden.orto.shared.base.mvi.BaseViewModel
import garden.orto.shared.base.mvi.UiState
import garden.orto.shared.domain.interactors.CreateNotesUseCase
import garden.orto.shared.domain.model.NoteState
import garden.orto.shared.domain.model.core.Resource
import org.koin.core.component.inject

open class NoteShareSheetViewModel :
    BaseViewModel<NoteShareSheetContract.Event, UiState, NoteShareSheetContract.Effect>() {
    private val createNotesUseCase: CreateNotesUseCase by inject()

    override fun createInitialState() = object : UiState {}

    override fun handleEvent(event: NoteShareSheetContract.Event) {
        when (event) {
            is NoteShareSheetContract.Event.CreateNotes -> createNotes(event.notes)
        }
    }

    private fun createNotes(notes: List<NoteState>) {
        collect(createNotesUseCase(notes)) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Success -> setEffect { NoteShareSheetContract.Effect.NotesCreated }
            }
        }
    }
}
