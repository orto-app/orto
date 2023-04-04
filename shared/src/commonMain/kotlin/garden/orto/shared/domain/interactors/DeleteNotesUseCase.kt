package garden.orto.shared.domain.interactors

import garden.orto.shared.domain.INoteRepository
import garden.orto.shared.domain.interactors.type.UseCaseIn

class DeleteNotesUseCase(
    private val noteRepository: INoteRepository,
    override val block: suspend (param: List<Long>) -> Unit = { noteRepository.deleteNotes(it) }
) : UseCaseIn<List<Long>>()