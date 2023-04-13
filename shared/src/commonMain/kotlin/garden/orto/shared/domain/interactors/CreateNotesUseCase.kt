package garden.orto.shared.domain.interactors

import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.exceptions.EmptyNoteException
import garden.orto.shared.domain.interactors.type.UseCaseIn
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import io.github.aakira.napier.Napier

class CreateNotesUseCase(
    private val noteRepository: IBlockRepository,
    private val mapper: NoteStateMapper,
    override val block: suspend (param: Iterable<String>) -> Unit = {
        val blocks = it.map { content ->
            if (content.isBlank()) {
                throw EmptyNoteException("Won't create an empty note!")
            }
            mapper.map(content)
        }
        Napier.d("mapped $it to $blocks")
        noteRepository.createNotes(blocks)
    }
) : UseCaseIn<Iterable<String>>()