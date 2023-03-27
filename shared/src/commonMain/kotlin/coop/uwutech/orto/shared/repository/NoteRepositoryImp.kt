package coop.uwutech.orto.shared.repository

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import coop.uwutech.orto.shared.domain.INoteRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NoteRepositoryImp :
    KoinComponent, INoteRepository {
    private val localData: ILocalData by inject()

    override fun createNote(note: Note, tags: Collection<Tag>) = localData.createNote(note, tags)
    override fun deleteNotes(noteIds: List<Long>) = localData.deleteNotes(noteIds)
    override val allNotes: Flow<List<Note>> = localData.getAllNotesAsFlow()

    override fun getNotesForTag(tagName: String) = localData.getNotesForTagAsFlow(tagName)
}