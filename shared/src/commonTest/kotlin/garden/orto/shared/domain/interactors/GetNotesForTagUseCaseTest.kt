package garden.orto.shared.domain.interactors

import garden.orto.TestUtil
import garden.orto.shared.domain.INoteRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.domain.model.NoteItemState
import garden.orto.shared.domain.model.core.Resource
import garden.orto.shared.domain.model.noteItemStateFromNote
import io.mockative.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotesForTagUseCaseTest : KoinTest {
    @Mock
    private val mockedNoteRepository = mock(classOf<INoteRepository>())

    @Mock
    private val mockedTagRepository = mock(classOf<ITagRepository>())
    private val getNotesForTagUseCase: GetNotesForTagUseCase by inject()

    private val homeNotes = listOf(
        TestUtil.makeNote(0),
        TestUtil.makeNote(1),
        TestUtil.makeNote(2),
        TestUtil.makeNote(3),
        TestUtil.makeNote(4),
    )


    // Given tag "home" frequencies are:
    // 2: 3/15
    // 3: 2/15
    // 4: 1/15
    // 5: 1/15
    // 24: 1/15
    private val tagsForNotes = listOf(
        listOf(TestUtil.makeTag(1, "home"), TestUtil.makeTag(2)),
        listOf(TestUtil.makeTag(1, "home"), TestUtil.makeTag(3), TestUtil.makeTag(24)),
        listOf(TestUtil.makeTag(1, "home"), TestUtil.makeTag(2), TestUtil.makeTag(4)),
        listOf(TestUtil.makeTag(1, "home")),
        listOf(
            TestUtil.makeTag(1, "home"),
            TestUtil.makeTag(2),
            TestUtil.makeTag(3),
            TestUtil.makeTag(5)
        )
    )
    private val expectedNoteItemStateListResource: Resource.Success<List<NoteItemState>> =
        Resource.Success(data = homeNotes.mapIndexed { i, n ->
            noteItemStateFromNote(n, tagsForNotes[i].filter { it.name != "home" }.map { it.name })
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

        // Define mocked behavior for repository
        given(mockedNoteRepository).function(mockedNoteRepository::getNotesForTag)
            .whenInvokedWith(any())
            .then { searchString ->
                when (searchString) {
                    "home" -> flowOf(homeNotes)
                    "trash" -> throw RuntimeException("error")
                    else -> flowOf(emptyList())
                }
            }
        given(mockedTagRepository).function(mockedTagRepository::getTagsForNote)
            .whenInvokedWith(any())
            .then { noteId -> flowOf(tagsForNotes[noteId.toInt()]) }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test getNotesForTagUseCase returns success with empty list`() = validTestCase(
        "random",
        emptyList()
    )

    @Test
    fun `test getNotesForTagUseCase returns success with non-empty list`() = validTestCase(
        "home",
        listOf(expectedNoteItemStateListResource)
    )

    @Test
    fun `test getNotesForTagUseCase returns error`() = invalidTestCase(
        "trash",
        listOf("error")
    )

    private fun validTestCase(testString: String, expected: List<Resource<List<NoteItemState>>>) = runTest {
        val result: List<Resource<List<NoteItemState>>> = getNotesForTagUseCase(testString).toList()
        assertEquals(expected.size, result.size)
        assertEquals(expected, result)
    }

    private fun invalidTestCase(testString: String, expected: List<String>) = runTest {
        val result: List<Resource.Error> = getNotesForTagUseCase(testString).toList() as List<Resource.Error>
        assertEquals(expected.size, result.size)
        assertEquals(expected, result.map { it.exception.message })
    }
}