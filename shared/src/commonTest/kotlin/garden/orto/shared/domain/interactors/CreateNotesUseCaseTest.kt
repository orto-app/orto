package garden.orto.shared.domain.interactors

import garden.orto.TestUtil
import garden.orto.shared.cache.Block
import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import io.mockative.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class CreateNotesUseCaseTest : KoinTest {
    @Mock
    private val mockedNoteRepository = mock(classOf<IBlockRepository>())
    private val createNotesUseCase: CreateNotesUseCase by inject()

    @BeforeTest
    fun setup() {
        // Initialize Koin container
        startKoin {
            modules(module {
                single { mockedNoteRepository }
                factory { NoteStateMapper() }
                factory { CreateNotesUseCase(get(), get()) }
            })
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `test createNotesUseCase returns success with simple content`() = validTestCase(
        listOf("random note"),
        listOf(emptySet())
    )

    @Test
    fun `test createNotesUseCase creates tags`() = validTestCase(
        listOf(" # Welcome to #Orto, your #\uD83E\uDD47 digital garden\n\n"),
        listOf(setOf("Orto,", "\uD83E\uDD47"))
    )

    private fun validTestCase(content: List<String>, expected: List<Set<String>>) = runTest {
        // Define mocked behavior for repository
        var blocks: List<Pair<Block, List<String>>>? = null
        given(mockedNoteRepository).function(mockedNoteRepository::createNotes)
            .whenInvokedWith(any())
            .then { pairs ->
                blocks = pairs.map { pair -> Pair(pair.first, pair.second.toList()) }
            }

        val result: List<Result<Unit>> = createNotesUseCase(content).toList()

        // Returns something
        assertNotNull(blocks)

        // A block is created for each note
        assertEquals(content.size, result.size)
        assertEquals(expected.size, result.size)
        assertEquals(expected.size, blocks!!.size)

        // Blocks content are ok
        assertEquals(content, blocks!!.map { it.first.content })

        // Tags are correctly parsed for each note
        assertEquals(expected, blocks!!.map { HashSet(it.second) })
    }

}