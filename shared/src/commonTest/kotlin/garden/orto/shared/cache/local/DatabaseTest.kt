package garden.orto.shared.cache.local

import garden.orto.TestUtil
import garden.orto.TestUtil.Companion.ALL_TAGS
import garden.orto.TestUtil.Companion.SINGLE_BLOCK
import garden.orto.TestUtil.Companion.SINGLE_TAG
import garden.orto.TestUtil.Companion.assertEqualsBlocks
import garden.orto.TestUtil.Companion.assertEqualsTag
import garden.orto.TestUtil.Companion.assertEqualsTags
import garden.orto.TestUtil.Companion.makeNow
import garden.orto.shared.cache.Tag
import garden.orto.shared.di.TestModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class DatabaseTest : KoinTest {
    private val localDataImp: LocalDataImp by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                TestModules.testDbModules
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `createTagChain creates a tag`() {
        val actual = localDataImp.createTagChain(SINGLE_TAG.name)
        assertEqualsTag(SINGLE_TAG, actual)
        assertEqualsTags(listOf(SINGLE_TAG), localDataImp.getAllTags())
    }

    @Test
    fun `createTagChain creates a chain`() {
        val name = "tech/workers/coalition"
        val expectedTags = listOf(
            Tag(
                id = 1,
                name = "tech",
                parent_id = null,
                created_at = makeNow(),
                updated_at = makeNow()
            ),
            Tag(
                id = 2,
                name = "tech/workers",
                parent_id = 1,
                created_at = makeNow(),
                updated_at = makeNow()
            ),
            Tag(
                id = 3,
                name = "tech/workers/coalition",
                parent_id = 2,
                created_at = makeNow(),
                updated_at = makeNow()
            )
        )

        localDataImp.createTagChain(name)

        assertEqualsTags(expectedTags, localDataImp.getAllTags())
    }

    @Test
    fun `createTagChain does not create duplicates`() {
        val name = "tech/workers/coalition"
        val expectedTags = listOf(
            Tag(
                id = 1,
                name = "tech",
                parent_id = null,
                created_at = makeNow(),
                updated_at = makeNow()
            ),
            Tag(
                id = 2,
                name = "tech/workers",
                parent_id = 1,
                created_at = makeNow(),
                updated_at = makeNow()
            ),
            Tag(
                id = 3,
                name = "tech/workers/coalition",
                parent_id = 2,
                created_at = makeNow(),
                updated_at = makeNow()
            )
        )

        localDataImp.createTagChain("tech/workers")
        localDataImp.createTagChain(name)

        assertEqualsTags(expectedTags, localDataImp.getAllTags())
    }

    @Test
    fun `createNote creates a block`() {
        localDataImp.createNote(SINGLE_BLOCK, ALL_TAGS.map { it.name })
        assertEqualsBlocks(listOf(SINGLE_BLOCK), localDataImp.getAllBlocks())
    }

    @Test
    fun `createNote creates a sequence of tags`() {
        localDataImp.createNote(SINGLE_BLOCK, ALL_TAGS.map { it.name })
        assertEqualsTags(ALL_TAGS, localDataImp.getAllTags())
    }

    @Test
    fun `createNote creates the relation between block and tags`() {
        localDataImp.createNote(SINGLE_BLOCK, ALL_TAGS.map { it.name })

        val tagList = localDataImp.getTagsForBlock(SINGLE_BLOCK.id)
        assertEqualsTags(ALL_TAGS, tagList)
    }

    @Test
    fun `createNote creates a reverse relation between tags and block`() {
        localDataImp.createNote(SINGLE_BLOCK, ALL_TAGS.map { it.name })
        for (tag in ALL_TAGS) {
            assertEqualsBlocks(listOf(SINGLE_BLOCK), localDataImp.getBlocksForTag(tag.name))
        }
    }

    @Test
    fun `deleteBlocks actually deletes blocks`() {
        for (idx in 1L..5) {
            localDataImp.createNote(TestUtil.makeBlock(idx), listOf(TestUtil.makeTag(idx).name))
        }
        localDataImp.deleteBlocks(listOf(2,4))

        assertEqualsBlocks(listOf(TestUtil.makeBlock(1L), TestUtil.makeBlock(3L), TestUtil.makeBlock(5L)), localDataImp.getAllBlocks())
    }

    @Test
    fun `deleteBlocks deletes also all relations of that block`() {
        for (idx in 1L..5) {
            localDataImp.createNote(TestUtil.makeBlock(idx), listOf(TestUtil.makeTag(idx).name))
        }
        localDataImp.deleteBlocks(listOf(2,4))

        assertEqualsTags(listOf(), localDataImp.getTagsForBlock(2))
        assertEqualsTags(listOf(), localDataImp.getTagsForBlock(4))
    }
}