package coop.uwutech.orto.shared.features.detail.mvi

import coop.uwutech.orto.shared.base.mvi.BaseViewModel
import coop.uwutech.orto.shared.base.mvi.BasicUiState
import coop.uwutech.orto.shared.domain.interactors.GetNotesForTagUseCase
import coop.uwutech.orto.shared.domain.interactors.DeleteNotesUseCase
import coop.uwutech.orto.shared.domain.model.NoteItemState
import coop.uwutech.orto.shared.domain.model.core.Resource
import org.koin.core.component.inject

open class TagDetailViewModel :
    BaseViewModel<TagDetailContract.Event, TagDetailContract.State, TagDetailContract.Effect>() {
    private val getNotesForTagUseCase: GetNotesForTagUseCase by inject()
    private val deleteNotesUseCase: DeleteNotesUseCase by inject()

    private var tagName: String? = null
    private lateinit var notes: List<NoteItemState>

    override fun createInitialState(): TagDetailContract.State =
        TagDetailContract.State(tagName = "", notes = BasicUiState.Idle)

    override fun handleEvent(event: TagDetailContract.Event) {
        when (event) {
            is TagDetailContract.Event.OnGetNotes -> getNotes(event.tagName)
            is TagDetailContract.Event.DeleteNotes -> deleteNotes(event.noteIds)
            TagDetailContract.Event.Retry -> tagName?.let { getNotes(it) }
        }
    }

    private fun getNotes(tagName: String) {
        this.tagName = tagName
        setState { copy(notes = BasicUiState.Loading) }
        collect(getNotesForTagUseCase(tagName)) { resource ->
            when (resource) {
                is Resource.Error -> setState { copy(notes = BasicUiState.Error()) }
                is Resource.Success -> {
                    setState { copy(notes = BasicUiState.Success(resource.data)) }
                    this.notes = resource.data
                }
            }
        }
    }

    private fun deleteNotes(noteIds: List<Long>) {
        collect(deleteNotesUseCase(noteIds)) { resource ->
            when (resource) {
                is Resource.Error -> setEffect { TagDetailContract.Effect.NotesDeletedError }
                is Resource.Success -> setEffect { TagDetailContract.Effect.NotesDeleted }
            }
        }
    }
}
