package garden.orto.shared.domain.model.mapper

import garden.orto.ofm.getTags
import garden.orto.shared.base.map.Mapper
import garden.orto.shared.cache.Block
import garden.orto.shared.domain.model.NoteState
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class NoteStateMapper : Mapper<NoteState, Pair<Block, List<String>>>() {
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

    override fun map(model: NoteState): Pair<Block, List<String>> = model.run {
        val tags = getTags(content)
        map(id, content, tags.toList())
    }

    fun map(content: String): Pair<Block, List<String>> = map(
        model = NoteState(
            // Unused
            id = -1L,
            content = content,
            tags = emptyList()
        )
    )


    override fun inverseMap(model: Pair<Block, List<String>>): NoteState = model.run {
        NoteState(first.id, first.content, second)
    }

    fun inverseMap(block: Block, tags: List<String>): NoteState = inverseMap(Pair(block, tags))
}