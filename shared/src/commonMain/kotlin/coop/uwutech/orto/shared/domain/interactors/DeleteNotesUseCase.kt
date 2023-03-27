package coop.uwutech.orto.shared.domain.interactors

import coop.uwutech.orto.shared.domain.INoteRepository
import coop.uwutech.orto.shared.domain.interactors.type.UseCaseIn

class DeleteNotesUseCase(
    private val noteRepository: INoteRepository,
    override val block: suspend (param: List<Long>) -> Unit = { noteRepository.deleteNotes(it) }
) : UseCaseIn<List<Long>>()