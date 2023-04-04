package garden.orto.shared.domain.model

import garden.orto.shared.cache.Note

fun noteItemStateFromNote(note: Note, tags: List<String>) = NoteItemState(
    id = note.id,
    title = note.title,
    url = note.url,
    image = note.image,
    content = note.content,
    tags = tags
)

data class NoteItemState(
    val id: Long,
    val title: String,
    val url: String?,
    val image: String?,
    val content: String?,
    val tags: List<String>
)