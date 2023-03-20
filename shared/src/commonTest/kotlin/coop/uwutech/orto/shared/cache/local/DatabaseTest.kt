package coop.uwutech.orto.shared.cache.local

import coop.uwutech.orto.TestUtil.Companion.ALL_TAGS
import coop.uwutech.orto.TestUtil.Companion.SINGLE_NOTE
import coop.uwutech.orto.TestUtil.Companion.SINGLE_TAG
import coop.uwutech.orto.shared.cache.local.Database
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
    private val database: Database by inject()

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                TestModules.testDbModules
            )
        }
//        MockProvider.register {
//            mockkClass(it)
//        }
//        declareMock<Database> {
//            every { getTags(any()) } returns(TestUtil.createTags(3))
//        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `insertTag creates a tag`() {
        database.insertTag(SINGLE_TAG)
        assertEquals(listOf(SINGLE_TAG), database.getAllTags().executeAsList())
    }


    @Test
    fun `createNote creates a note`() {
        database.createNote(SINGLE_NOTE, ALL_TAGS)
        assertEquals(listOf(SINGLE_NOTE), database.getAllNotes().executeAsList())
    }

    @Test
    fun `createNote creates a sequence of tags`() {
        database.createNote(SINGLE_NOTE, ALL_TAGS)

        val tagList = HashSet(database.getAllTags().executeAsList())
        assertEquals(ALL_TAGS.size, tagList.size)
        assertEquals(ALL_TAGS, tagList)
    }

    @Test
    fun `createNote creates the relation between note and tags`() {
        database.createNote(SINGLE_NOTE, ALL_TAGS)

        val tagList = HashSet(database.getTags(SINGLE_NOTE).executeAsList())
        assertEquals(ALL_TAGS, tagList)
    }

    @Test
    fun `createNote creates a reverse relation between tags and note`() {
        database.createNote(SINGLE_NOTE, ALL_TAGS)
        for (tag in ALL_TAGS) {
            assertEquals(listOf(SINGLE_NOTE), database.getNotes(tag).executeAsList())
        }
    }
}