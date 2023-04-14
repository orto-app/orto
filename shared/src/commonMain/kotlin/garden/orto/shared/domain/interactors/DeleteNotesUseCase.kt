package garden.orto.shared.domain.interactors

import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.interactors.type.UseCaseIn

class DeleteNotesUseCase(
    private val noteRepository: IBlockRepository,
    override val block: suspend (param: List<Long>) -> Unit = { noteRepository.deleteBlocks(it) }
) : UseCaseIn<List<Long>>()