package coop.uwutech.orto.shared.domain.interactors

import coop.uwutech.orto.TestUtil
import coop.uwutech.orto.shared.domain.INoteRepository
import coop.uwutech.orto.shared.domain.ITagRepository
import coop.uwutech.orto.shared.domain.model.NoteItemState
import coop.uwutech.orto.shared.domain.model.core.Resource
import coop.uwutech.orto.shared.domain.model.noteItemStateFromNote
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProvider
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotesForTagUseCaseTest : KoinTest {
    private val mockedNoteRepository = mockkClass(INoteRepository::class)
    private val mockedTagRepository = mockkClass(ITagRepository::class)
    private val getNotesForTagUseCase: GetNotesForTagUseCase by inject()

    private val homeNotes = listOf(TestUtil.makeNote(0), TestUtil.makeNote(1))
    private val tagsForNotes = listOf(
        listOf(TestUtil.makeTag(1), TestUtil.makeTag(1)),
        listOf(TestUtil.makeTag(2))
    )
    private val expectedNoteItemStateListResource: Resource.Success<List<NoteItemState>> =
        Resource.Success(data = homeNotes.mapIndexed { i, n ->
            noteItemStateFromNote(n, tagsForNotes[i].map { it.name })
        })

    @BeforeTest
    fun setup() {
        // Initialize Koin container
        startKoin {
            modules(module {
                single { mockedNoteRepository }
                single { mockedTagRepository }
                factory { GetNotesForTagUseCase(get(), get()) }
            })
        }

        MockProvider.register {
            mockkClass(it)
        }

        // Define mocked behavior for repository
        every { mockedNoteRepository.getNotesForTag(any()) } answers { invocation ->
            val searchString = firstArg<String>()
            when (searchString) {
                "home" -> flowOf(homeNotes)
                "trash" -> throw RuntimeException("error")
                else -> flowOf(emptyList())
            }
        }

        every { mockedTagRepository.getTagsForNote(any()) } answers { invocation ->
            val noteId = firstArg<Long>()
            flowOf(tagsForNotes[noteId.toInt()])
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test getNotesForTagUseCase returns success with empty list`() = runTest {
        val testString = "random"
        val result: List<Resource<List<NoteItemState>>> = getNotesForTagUseCase(testString).toList()
        assertEquals(emptyList(), result)
    }

    @Test
    fun `test getNotesForTagUseCase returns success with non-empty list`() = runTest {
        val testString = "home"
        val result: List<Resource<List<NoteItemState>>> = getNotesForTagUseCase(testString).toList()
        assertEquals(1, result.size)
        assertTrue { result[0] is Resource.Success<List<NoteItemState>> }
        assertEquals(expectedNoteItemStateListResource, result[0])
    }

    @Test
    fun `test getNotesForTagUseCase returns error`() = runTest {
        val testString = "trash"
        val result: List<Resource<List<NoteItemState>>> = getNotesForTagUseCase(testString).toList()
        assertEquals(1, result.size)


        val error = result[0] as Resource.Error
        assertEquals("error", error.exception.message)
    }
}