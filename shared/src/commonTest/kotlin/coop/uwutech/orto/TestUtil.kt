package coop.uwutech.orto

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class TestUtil {
    companion object {
        val SINGLE_NOTE = Note(
            id = "cb4da50b-0b73-4716-a4a8-d77ca297b454",
            title = "A nice note",
            url = "https://orto.uwutech.coop/feetpix",
            image = null,
            content = "Nice content"
        )

        val SINGLE_TAG = Tag(id="63a225ba-d0da-4163-8910-9243b46d801c", name="apples", parent_id = null)

        val ALL_TAGS = setOf(
            SINGLE_TAG,
            Tag(id="414e2fa6-de2f-4080-8591-e54221b0fe7e", name="oranges", parent_id = null),
            Tag(id="ba66fc3c-10a4-4702-8177-a86e409a4b2c", name="bananas", parent_id = null)
        )

        fun createNote(id: String): Note {
            return Note(
                id = id,
                title = Random.nextInt().toString(),
                url = Random.nextInt().toString(),
                image = null,
                content = Random.nextInt().toString()
            )
        }

        fun createTag(id: String): Tag {
            return Tag(id = id, name = Random.nextInt().toString(), parent_id = null)
        }

        fun createTags(times: Int) = flow {
            for (i in 1..times) {
                emit(createTag(i.toString()))
            }
        }
    }
}