package coop.uwutech.orto.shared.cache

import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NoteRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default) :
    KoinComponent {
    private val scope = CoroutineScope(ioDispatcher)
    private val database: Database by inject()

    fun initialize() = scope.async {
        // ...
    }

    fun insertNote(note: Note) = database.insertNote(note)

    fun createNote(note: Note, tags: Collection<Tag>) = database.createNote(note, tags)

    val allNotes = database.getAllNotesAsFlow()

    fun getNotesForTag(tag: Tag) = database.getNotesAsFlow(tag)
}