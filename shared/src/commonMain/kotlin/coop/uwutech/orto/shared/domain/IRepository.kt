package coop.uwutech.orto.shared.domain

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import kotlinx.coroutines.flow.Flow

interface INoteRepository {
    fun createNote(note: Note, tags: Collection<Tag>)
    fun deleteNotes(noteIds: List<Long>)
    val allNotes: Flow<List<Note>>
    fun getNotesForTag(tagName: String): Flow<List<Note>>
}

interface ITagRepository {
    fun insertTag(tag: Tag)
    val allTags: Flow<List<Tag>>
    fun getTagsForNote(id: Long): Flow<List<Tag>>
}