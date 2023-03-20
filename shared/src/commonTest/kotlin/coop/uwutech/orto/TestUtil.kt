package coop.uwutech.orto

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

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
        fun makeTag(long: Long) = Tag(long, "Note${long}", null)
    }
}