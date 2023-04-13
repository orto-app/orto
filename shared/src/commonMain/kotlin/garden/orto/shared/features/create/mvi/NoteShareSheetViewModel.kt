package garden.orto.shared.features.create.mvi

import garden.orto.shared.base.mvi.BaseViewModel
import garden.orto.shared.base.mvi.BasicUiState
import garden.orto.shared.domain.interactors.CreateNotesUseCase
import garden.orto.shared.domain.interactors.ParseNoteContentUseCase
import garden.orto.shared.domain.model.NoteState
import io.github.aakira.napier.Napier
import org.koin.core.component.inject

open class NoteShareSheetViewModel :
    BaseViewModel<NoteShareSheetContract.Event, NoteShareSheetContract.State, NoteShareSheetContract.Effect>() {
    private val createNotesUseCase: CreateNotesUseCase by inject()
    private val parseNoteContentUseCase: ParseNoteContentUseCase by inject()

    private lateinit var note: NoteState

    override fun createInitialState(): NoteShareSheetContract.State {
        this.note = NoteState(id = -1L, content = "", tags = emptyList())
        return NoteShareSheetContract.State(note = BasicUiState.Success(this.note))
    }

    override fun handleEvent(event: NoteShareSheetContract.Event) {
        when (event) {
            is NoteShareSheetContract.Event.CreateNote -> createNote(event.content)
            is NoteShareSheetContract.Event.OnContentChanged -> onContentChanged(event.content)
            NoteShareSheetContract.Event.OnBackPressed -> setEffect {
                NoteShareSheetContract.Effect.BackNavigation
            }
        }
    }

    private fun createNote(content: String) {
        collect(createNotesUseCase(listOf(content))) { result ->
            result.onSuccess {
                setEffect { NoteShareSheetContract.Effect.NotesCreated }
            }.onFailure {
                setEffect { NoteShareSheetContract.Effect.NotesCreatedFailure(it.message) }
            }
        }
    }

    private fun onContentChanged(content: String) {
        setState { copy(note = BasicUiState.Loading) }
        collect(parseNoteContentUseCase(content)) { result ->
            result
                .onSuccess {
                    Napier.d("parsed ${it.tags} from ${it.content}")
                    setState {
                        copy(
                            note = BasicUiState.Success(it)
                        )
                    }
                    this.note = it
                }
                .onFailure {
                    setState { copy(note = BasicUiState.Error()) }
                }
        }
    }
}
