package garden.orto.shared.domain.interactors

import garden.orto.ofm.getTags
import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.interactors.type.UseCaseIn
import garden.orto.shared.domain.model.NoteState
import garden.orto.shared.domain.model.mapper.NoteStateMapper

class CreateNotesUseCase(
    private val noteRepository: IBlockRepository,
    private val mapper: NoteStateMapper,
    override val block: suspend (param: Iterable<NoteState>) -> Unit = {
        val blocks = it.map { noteState ->
            val tags = getTags(noteState.content)
            mapper.map(noteState, tags.toList())
        }
        noteRepository.createNotes(blocks)
    }
) : UseCaseIn<Iterable<NoteState>>()