package garden.orto

import garden.orto.shared.cache.Block
import garden.orto.shared.cache.Tag
import kotlinx.datetime.LocalDateTime
import kotlin.test.assertEquals

class TestUtil {
    companion object {
        fun makeNow() = LocalDateTime.parse("2021-03-27T02:16:20")

        val SINGLE_BLOCK = Block(
            id = 1,
            content = "Nice content",
            makeNow(),
            makeNow()
        )

        val SINGLE_TAG = Tag(id = 1, name = "apples", parent_id = null, makeNow(), makeNow())

        val ALL_TAGS = listOf(
            SINGLE_TAG,
            Tag(id = 2, name = "oranges", parent_id = null, makeNow(), makeNow()),
            Tag(id = 3, name = "bananas", parent_id = null, makeNow(), makeNow())
        )

        fun makeBlock(long: Long) = Block(long, "content${long}", makeNow(), makeNow())
        fun makeTag(long: Long) = makeTag(long, "Tag${long}")
        fun makeTag(long: Long, name: String) = Tag(long, name, null, makeNow(), makeNow())
        
        fun assertEqualsTag(expected: Tag, actual: Tag) {
            assertEquals(expected.id, actual.id)
            assertEquals(expected.name, actual.name)
            assertEquals(expected.parent_id, actual.parent_id)
        }

        fun assertEqualsTags(expected: Collection<Tag>, actual: Collection<Tag>) {
            assertEquals(expected.size, actual.size)
            expected.zip(actual) { e, a ->
                assertEqualsTag(e, a)
            }
        }

        fun assertEqualsBlock(expected: Block, actual: Block) {
            assertEquals(expected.id, actual.id)
            assertEquals(expected.content, actual.content)
        }

        fun assertEqualsBlocks(expected: Collection<Block>, actual: Collection<Block>) {
            assertEquals(expected.size, actual.size)
            expected.zip(actual) { e, a ->
                assertEqualsBlock(e, a)
            }
        }
    }
}