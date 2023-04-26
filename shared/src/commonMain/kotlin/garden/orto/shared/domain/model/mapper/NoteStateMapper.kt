package garden.orto.shared.domain.model.mapper

import garden.orto.shared.base.map.Mapper
import garden.orto.shared.cache.Block
import garden.orto.shared.domain.model.NoteState
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NoteStateMapper : Mapper<NoteState, Pair<Block, List<String>>>() {
    override fun map(model: NoteState): Pair<Block, List<String>> = model.run {
        map(id, content, tags)
    }

    fun map(model: NoteState, tags: List<String>): Pair<Block, List<String>> = model.run {
        map(id, content, tags)
    }

    private fun map(id: Long, content: String, tags: List<String>): Pair<Block, List<String>> = Pair(
            Block(
                id,
                content,
                // FIXME: We are forced to pass something here even
                // FIXME: if it doesn't exactly make sense.
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            ), tags
        )


    override fun inverseMap(model: Pair<Block, List<String>>): NoteState = model.run {
        NoteState(first.id, first.content, second)
    }

    fun inverseMap(block: Block, tags: List<String>): NoteState = inverseMap(Pair(block, tags))
}