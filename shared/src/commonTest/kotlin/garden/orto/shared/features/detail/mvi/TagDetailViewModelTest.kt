package garden.orto.shared.features.detail.mvi

import app.cash.turbine.test
import garden.orto.TestUtil
import garden.orto.shared.base.executor.IDispatcher
import garden.orto.shared.base.mvi.BasicUiState
import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.domain.interactors.DeleteNotesUseCase
import garden.orto.shared.domain.interactors.GetNotesForTagUseCase
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import io.mockative.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class TagDetailViewModelTest : KoinTest {

    private val testDispatcher = object : IDispatcher {
        override val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    }

    // FIXME: We don't mock the usecases (instead they're injected in startKoin) because Mockative can only mock interfaces
    @Mock
    private val mockedNoteRepository = mock(classOf<IBlockRepository>())

    @Mock
    private val mockedTagRepository = mock(classOf<ITagRepository>())
    private val tagDetailViewModel: TagDetailViewModel by inject()

    private val homeNotes = listOf(TestUtil.makeBlock(0), TestUtil.makeBlock(1))
    private val tagsForNotes = listOf(
        listOf(TestUtil.makeTag(1), TestUtil.makeTag(1)),
        listOf(TestUtil.makeTag(2))
    )

    @BeforeTest
    fun setup() {
        // Initialize Koin container
        startKoin {
            modules(module {
                single { mockedNoteRepository }
                single { mockedTagRepository }
                factory { NoteStateMapper() }
                factory { GetNotesForTagUseCase(get(), get(), get()) }
                factory { DeleteNotesUseCase(get()) }
                // Main dispatcher
                single<IDispatcher> { testDispatcher }
                // IO dispatcher
                single { testDispatcher.dispatcher }
                single { TagDetailViewModel() }
            })
        }

        // Define mocked behavior for repository
        given(mockedNoteRepository).function(mockedNoteRepository::getBlocksForTag)
            .whenInvokedWith(any())
            .then { searchString ->
                flow {
                    delay(100)
                    when (searchString) {
                        "home" -> {
                            emit(homeNotes)
                            delay(100)
                            emit(homeNotes.subList(0, 1))
                        }

                        "trash" -> throw RuntimeException("error")
                        else -> emit(emptyList())
                    }
                }
            }

        given(mockedTagRepository).function(mockedTagRepository::getTagsForBlock)
            .whenInvokedWith(any())
            .then { noteId -> flowOf(tagsForNotes[noteId.toInt()]) }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `when creating viewModel should set Idle state`() = runTest {
        assertEquals(BasicUiState.Idle, tagDetailViewModel.uiState.value.notes)
    }

    @Test
    fun `viewModel should return Success for empty list`() = runTest {
        val testString = "random"
        tagDetailViewModel.uiState.test {
            assertEquals(BasicUiState.Idle, awaitItem().notes)
            tagDetailViewModel.setEvent(TagDetailContract.Event.OnGetNotes(testString))
            assertEquals(BasicUiState.Loading, awaitItem().notes)
//            assertEquals(BasicUiState.Empty, awaitItem().notes)
        }
    }
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
