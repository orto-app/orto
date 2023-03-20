package coop.uwutech.orto.shared.cache.local

import coop.uwutech.orto.TestUtil.Companion.ALL_TAGS
import coop.uwutech.orto.TestUtil.Companion.SINGLE_NOTE
import coop.uwutech.orto.TestUtil.Companion.SINGLE_TAG
import coop.uwutech.orto.shared.di.TestModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

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
    fun `insertTag creates a tag`() {
        localDataImp.insertTag(SINGLE_TAG)
        assertEquals(listOf(SINGLE_TAG), localDataImp.getAllTags())
    }


    @Test
    fun `createNote creates a note`() {
        localDataImp.createNote(SINGLE_NOTE, ALL_TAGS)
        assertEquals(listOf(SINGLE_NOTE), localDataImp.getAllNotes())
    }

    @Test
    fun `createNote creates a sequence of tags`() {
        localDataImp.createNote(SINGLE_NOTE, ALL_TAGS)

        val tagList = HashSet(localDataImp.getAllTags())
        assertEquals(ALL_TAGS.size, tagList.size)
        assertEquals(ALL_TAGS, tagList)
    }

    @Test
    fun `createNote creates the relation between note and tags`() {
        localDataImp.createNote(SINGLE_NOTE, ALL_TAGS)

        val tagList = HashSet(localDataImp.getTagsForNote(SINGLE_NOTE.id))
        assertEquals(ALL_TAGS, tagList)
    }

    @Test
    fun `createNote creates a reverse relation between tags and note`() {
        localDataImp.createNote(SINGLE_NOTE, ALL_TAGS)
        for (tag in ALL_TAGS) {
            assertEquals(listOf(SINGLE_NOTE), localDataImp.getNotesForTag(tag.name))
        }
    }
}