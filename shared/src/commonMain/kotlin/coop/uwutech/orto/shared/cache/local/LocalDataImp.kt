package coop.uwutech.orto.shared.cache.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.OrtoDatabase
import coop.uwutech.orto.shared.cache.Tag
import coop.uwutech.orto.shared.repository.ILocalData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

internal class LocalDataImp(databaseDriver: SqlDriver) : KoinComponent, ILocalData {
    private val database = OrtoDatabase(databaseDriver)
    private val dbQuery = database.ortoDatabaseQueries

    override fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRelations()
            dbQuery.removeAllTags()
            dbQuery.removeAllNotes()
        }
    }

    override fun insertTag(tag: Tag) {
        dbQuery.insertTag(
            id = tag.id,
            name = tag.name,
            parent_id = tag.parent_id
        )
    }

    override fun insertNote(note: Note) {
        dbQuery.insertNote(
            id = note.id,
            title = note.title,
            url = note.url,
            image = note.image,
            content = note.content
        )
    }

    override fun createNote(note: Note, tags: Collection<Tag>) {
        dbQuery.transaction {
            insertNote(note)
            tags.forEach {
                val tag = dbQuery.getTagByName(it.name).executeAsOneOrNull()
                if (tag == null) {
                    insertTag(it)
                }
                val inserted = dbQuery.getTagByName(it.name).executeAsOne()
                dbQuery.insertNoteTagRelation(
                    note.id,
                    inserted.id
                )
            }
        }
    }

    private fun _getAllNotes() = dbQuery.getAllNotes()
    fun getAllNotes() = _getAllNotes().executeAsList()

    override fun getAllNotesAsFlow(): Flow<List<Note>> {
        return _getAllNotes()
            .asFlow()
            .mapToList()
    }

    private fun _getNotesForTag(name: String): Query<Note> {
        return dbQuery.getNotesForTag(name = name)
    }

    fun getNotesForTag(name: String): List<Note> {
        return _getNotesForTag(name = name).executeAsList()
    }

    override fun getNotesForTagAsFlow(tagName: String): Flow<List<Note>> {
        return _getNotesForTag(tagName)
            .asFlow()
            .mapToList()
    }

    private fun _getAllTags() = dbQuery.getAllTags()

    fun getAllTags(): List<Tag> = _getAllTags().executeAsList()

    override fun getAllTagsAsFlow(): Flow<List<Tag>> {
        return _getAllTags()
            .asFlow()
            .mapToList()
    }

    private fun _getTagsForNote(id: Long) = dbQuery.getTagsForNote(note_id = id)
    fun getTagsForNote(id: Long): List<Tag> = _getTagsForNote(id).executeAsList()

    override fun getTagsForNoteAsFlow(id: Long): Flow<List<Tag>> {
        return _getTagsForNote(id)
            .asFlow()
            .mapToList()
    }
}