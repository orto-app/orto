package garden.orto.shared.features.detail.mvi

import garden.orto.shared.base.mvi.BaseViewModel
import garden.orto.shared.base.mvi.BasicUiState
import garden.orto.shared.domain.interactors.GetNotesForTagUseCase
import garden.orto.shared.domain.interactors.DeleteNotesUseCase
import garden.orto.shared.domain.model.NoteState
import org.koin.core.component.inject

open class TagDetailViewModel :
    BaseViewModel<TagDetailContract.Event, TagDetailContract.State, TagDetailContract.Effect>() {
    private val getNotesForTagUseCase: GetNotesForTagUseCase by inject()
    private val deleteNotesUseCase: DeleteNotesUseCase by inject()

    private var tagName: String? = null
    private lateinit var notes: List<NoteState>

    override fun createInitialState(): TagDetailContract.State =
        TagDetailContract.State(tagName = "", notes = BasicUiState.Idle)

    override fun handleEvent(event: TagDetailContract.Event) {
        when (event) {
            is TagDetailContract.Event.OnGetNotes -> getNotes(event.tagName)
            is TagDetailContract.Event.DeleteNotes -> deleteNotes(event.noteIds)
            is TagDetailContract.Event.OnNoteClick -> setEffect {
                TagDetailContract.Effect.NavigateToDetailNote(event.idNote)
            }
            is TagDetailContract.Event.OnFABClick -> setEffect {
                TagDetailContract.Effect.NavigateToCreateNote
            }
            TagDetailContract.Event.Retry -> tagName?.let { getNotes(it) }
        }
    }

    private fun getNotes(tagName: String) {
        this.tagName = tagName
        setState { copy(tagName = tagName, notes = BasicUiState.Loading) }
        collect(getNotesForTagUseCase(tagName)) { result ->
            result
                .onSuccess {
                    setState {
                        copy(
                            notes =
                            if (it.isEmpty())
                                BasicUiState.Empty
                            else
                                BasicUiState.Success(it)
                        )
                    }
                    this.notes = it
                }
                .onFailure {
                    setState { copy(notes = BasicUiState.Error()) }
                }
        }
    }

    private fun deleteNotes(noteIds: List<Long>) {
        collect(deleteNotesUseCase(noteIds)) { result ->
            result.onSuccess {
                setEffect { TagDetailContract.Effect.NotesDeleted }
            }
        }
    }
}
