package garden.orto.shared.domain.interactors

import app.cash.turbine.test
import garden.orto.TestUtil
import garden.orto.shared.base.executor.IDispatcher
import garden.orto.shared.cache.Block
import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.domain.model.NoteState
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import io.mockative.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotesForTagUseCaseTest : KoinTest {
    private val testDispatcher = object : IDispatcher {
        override val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    }
    @Mock
    private val mockedNoteRepository = mock(classOf<IBlockRepository>())

    @Mock
    private val mockedTagRepository = mock(classOf<ITagRepository>())
    private val getNotesForTagUseCase: GetNotesForTagUseCase by inject()

    private val homeNotes = listOf(
        TestUtil.makeBlock(0),
        TestUtil.makeBlock(1),
        TestUtil.makeBlock(2),
        TestUtil.makeBlock(3),
        TestUtil.makeBlock(4),
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
    private val expectedNoteStateListResource: Result<List<NoteState>> =
        Result.success(homeNotes.mapIndexed { i, n ->
            val mapper = NoteStateMapper()
            mapper.inverseMap(n, tagsForNotes[i].filter { it.name != "home" }.map { it.name })
        })

    @BeforeTest
    fun setup() {
        // Initialize Koin container
        startKoin {
            modules(module {
                single { mockedNoteRepository }
                single { mockedTagRepository }
                // Main dispatcher
                single<IDispatcher> { testDispatcher }
                // IO dispatcher
                single { testDispatcher.dispatcher }
                factory { NoteStateMapper() }
                factory { GetNotesForTagUseCase(get(), get(), get()) }
            })
        }

        // Define mocked behavior for repository
        given(mockedNoteRepository).function(mockedNoteRepository::getBlocksForTag)
            .whenInvokedWith(any())
            .then { searchString ->
                when (searchString) {
                    "home" -> flow {
                        emit(homeNotes)
                        delay(100)
                        emit(homeNotes.subList(0, 1))
                    }.flowOn(testDispatcher.dispatcher)

                    "trash" -> throw RuntimeException("error")
                    else -> flowOf<List<Block>>(emptyList()).flowOn(testDispatcher.dispatcher)
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
    fun `test getNotesForTagUseCase returns success with empty list`() = validTestCase(
        "random",
        listOf(Result.success(emptyList()))
    )

    @Test
    fun `test getNotesForTagUseCase returns success with non-empty list`() = validTestCase(
        "home",
        listOf(
            expectedNoteStateListResource,
            Result.success(expectedNoteStateListResource.getOrNull()!!.subList(0, 1)))
    )

    @Test
    fun `test getNotesForTagUseCase returns error`() = invalidTestCase(
        "trash",
        listOf("error")
    )

    private fun validTestCase(testString: String, expected: List<Result<List<NoteState>>>) = runTest(testDispatcher.dispatcher) {
        val testFlow: MutableStateFlow<Result<List<NoteState>>> = MutableStateFlow(Result.success(emptyList()))
        launch {
            getNotesForTagUseCase(testString)
                .flowOn(testDispatcher.dispatcher)
                .collect {
                    testFlow.value = it
                }
        }
        testFlow.test {
            for (r in expected) {
                advanceTimeBy(1000)
                assertEquals(r, awaitItem())
            }
        }
    }

    private fun invalidTestCase(testString: String, expected: List<String>) = runTest {
        val result = getNotesForTagUseCase(testString).toList()
        assertTrue(result.all { it.isFailure })
        assertEquals(expected.size, result.size)
        assertEquals(expected, result.map { it.exceptionOrNull()!!.message })
    }
}