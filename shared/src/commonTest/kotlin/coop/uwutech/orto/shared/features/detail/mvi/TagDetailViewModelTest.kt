package garden.orto.shared.features.detail.mvi

import garden.orto.TestUtil
import garden.orto.shared.base.executor.IDispatcher
import garden.orto.shared.base.mvi.BasicUiState
import garden.orto.shared.domain.INoteRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.domain.interactors.DeleteNotesUseCase
import garden.orto.shared.domain.interactors.GetNotesForTagUseCase
import garden.orto.shared.domain.model.NoteItemState
import garden.orto.shared.domain.model.core.Resource
import garden.orto.shared.domain.model.noteItemStateFromNote
import io.mockative.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class TagDetailViewModelTest : KoinTest {
    class TestDispatcher : IDispatcher {
        override val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    }

    // FIXME: We don't mock the usecases (instead they're injected in startKoin) because Mockative can only mock interfaces
    @Mock
    private val mockedNoteRepository = mock(classOf<INoteRepository>())

    @Mock
    private val mockedTagRepository = mock(classOf<ITagRepository>())
    private val tagDetailViewModel: TagDetailViewModel by inject()

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
                factory { DeleteNotesUseCase(get()) }
                single<IDispatcher> { TestDispatcher() }
                single<CoroutineDispatcher> { StandardTestDispatcher() }
                single { TagDetailViewModel() }
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

//    @Test
//    fun `test tagDetailViewModel returns success with empty list`() = runTest {
//        val testString = "random"
//
//        assertEquals(BasicUiState.Idle, tagDetailViewModel.uiState.value.notes)
//
//        val job = launch {
//            tagDetailViewModel.uiState.collect() //now it should work
//        }
//
//        tagDetailViewModel.setEvent(TagDetailContract.Event.OnGetNotes(testString))
//        assertEquals(BasicUiState.Loading, tagDetailViewModel.uiState.value.notes)
//        advanceTimeBy(5000) // This is required in order to bypass debounce(500)
//        runCurrent() // Run any pending tasks at the current virtual time, according to the testScheduler.
//
//        assertEquals(BasicUiState.Empty, tagDetailViewModel.uiState.value.notes)
//
//        job.cancel()
//    }
//
//    @Test
//    fun `test getNotesForTagUseCase returns success with non-empty list`() = runTest {
//        val testString = "home"
//
//        val result: List<Resource<List<NoteItemState>>> = getNotesForTagUseCase(testString).toList()
//        assertEquals(1, result.size)
//        assertTrue { result[0] is Resource.Success<List<NoteItemState>> }
//        assertEquals(expectedNoteItemStateListResource, result[0])
//    }
//
//    @Test
//    fun `test getNotesForTagUseCase returns error`() = runTest {
//        val testString = "trash"
//        val result: List<Resource<List<NoteItemState>>> = getNotesForTagUseCase(testString).toList()
//        assertEquals(1, result.size)
//
//
//        val error = result[0] as Resource.Error
//        assertEquals("error", error.exception.message)
//    }
}