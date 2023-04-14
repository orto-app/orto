package garden.orto.shared.domain.model

data class NoteState(
    val id: Long,
    val content: String,
    val tags: List<String>
)