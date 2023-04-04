package garden.orto

import garden.orto.shared.cache.Note
import garden.orto.shared.cache.Tag

class TestUtil {
    companion object {
        val SINGLE_NOTE = Note(
            id = 0,
            title = "A nice note",
            url = "https://orto.uwutech.coop/feetpix",
            image = null,
            content = "Nice content"
        )

        val SINGLE_TAG = Tag(id=0, name="apples", parent_id = null)

        val ALL_TAGS = setOf(
            SINGLE_TAG,
            Tag(id=1, name="oranges", parent_id = null),
            Tag(id=2, name="bananas", parent_id = null)
        )

        fun makeNote(long: Long) = Note(long, "Note${long}", "url${long}", "img${long}", "content${long}")
        fun makeTag(long: Long) = makeTag(long, "Tag${long}")
        fun makeTag(long: Long, name: String) = Tag(long, name, null)
    }
}