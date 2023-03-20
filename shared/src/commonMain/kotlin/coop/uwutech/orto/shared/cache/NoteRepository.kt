package coop.uwutech.orto.shared.cache

import coop.uwutech.orto.shared.cache.local.Database
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NoteRepository() :
    KoinComponent {
    private val database: Database by inject()

    fun insertNote(note: Note) = database.insertNote(note)

    fun createNote(note: Note, tags: Collection<Tag>) = database.createNote(note, tags)

    val allNotes: Flow<List<Note>> = database.getAllNotesAsFlow()

    fun getNotesForTag(tag: Tag) = database.getNotesAsFlow(tag)
}