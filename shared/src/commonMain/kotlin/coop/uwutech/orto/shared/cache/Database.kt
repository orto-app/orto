package coop.uwutech.orto.shared.cache

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

internal class Database(databaseDriver: SqlDriver): KoinComponent {
    private val database = OrtoDatabase(databaseDriver)
    private val dbQuery = database.ortoDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRelations()
            dbQuery.removeAllTags()
            dbQuery.removeAllNotes()
        }
    }

    internal fun insertTag(tag: Tag) {
        dbQuery.insertTag(
            id = tag.id,
            name = tag.name
        )
    }

    internal fun insertNote(note: Note) {
        dbQuery.insertNote(
            id = note.id,
            title = note.title,
            url = note.url,
            image = note.image,
            content = note.content
        )
    }

    internal fun createNote(note: Note, tags: Collection<Tag>) {
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

    internal fun getAllNotes() = dbQuery.getAllNotes()

    internal fun getAllNotesAsFlow(): Flow<Note> {
        return getAllNotes()
            .asFlow()
            .mapToOne()
    }

    internal fun getNotes(tag: Tag) = dbQuery.getNotesForTag(tag_id = tag.id)

    internal fun getNotesAsFlow(tag: Tag): Flow<Note> {
        return getNotes(tag)
            .asFlow()
            .mapToOne()
    }

    internal fun getAllTags() = dbQuery.getAllTags()

    internal fun getAllTagsAsFlow(): Flow<Tag> {
        return getAllTags()
            .asFlow()
            .mapToOne()
    }

    internal fun getTags(note: Note) = dbQuery.getTagsForNote(note_id = note.id)

    internal fun getTagsAsFlow(note: Note): Flow<Tag> {
        return getTags(note)
            .asFlow()
            .mapToOne()
    }
}