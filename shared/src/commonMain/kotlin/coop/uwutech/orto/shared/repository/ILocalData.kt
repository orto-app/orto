package coop.uwutech.orto.shared.repository

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import kotlinx.coroutines.flow.Flow

interface ILocalData {
    fun clearDatabase()

    fun insertTag(tag: Tag)

    fun insertNote(note: Note)

    fun deleteNotes(noteIds: List<Long>)

    fun createNote(note: Note, tags: Collection<Tag>)

    fun getAllNotesAsFlow(): Flow<List<Note>>

    fun getNotesForTagAsFlow(tagName: String): Flow<List<Note>>

    fun getAllTagsAsFlow(): Flow<List<Tag>>

    fun getTagsForNoteAsFlow(id: Long): Flow<List<Tag>>
}