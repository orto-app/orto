package garden.orto.shared.domain.interactors

import garden.orto.shared.domain.interactors.type.UseCaseInOutFlow
import garden.orto.shared.domain.model.NoteState
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import kotlinx.coroutines.flow.flow

class ParseNoteContentUseCase(
    private val mapper: NoteStateMapper,
) : UseCaseInOutFlow<String, NoteState>() {
    override fun build(param: String) = flow {
        val pair = mapper.map(param)
        emit(mapper.inverseMap(pair))
    }
}