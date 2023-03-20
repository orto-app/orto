package coop.uwutech.orto.shared.domain.interactors

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import coop.uwutech.orto.shared.domain.INoteRepository
import coop.uwutech.orto.shared.domain.ITagRepository
import coop.uwutech.orto.shared.domain.interactors.type.UseCaseInOutFlow
import coop.uwutech.orto.shared.domain.model.NoteItemState
import coop.uwutech.orto.shared.domain.model.noteItemStateFromNote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.zip

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotesForTagUseCase(
    private val noteRepository: INoteRepository,
    private val tagRepository: ITagRepository
) : UseCaseInOutFlow<String, List<NoteItemState>>() {
    override fun build(param: String): Flow<List<NoteItemState>> {
        val notes: Flow<List<Note>> = noteRepository.getNotesForTag(param)
        val tags: Flow<List<List<Tag>>> = notes.flatMapLatest { noteList ->
            // Create a Flow of List<Tag> for each Note
            val tagFlowList = noteList.map { note ->
                tagRepository.getTagsForNote(note.id)
            }
            // Combine all the List<Tag> flows into one Flow<List<List<Tag>>>
            combine(tagFlowList) { tagListArray -> tagListArray.toList() }
        }
        return tags.zip(notes) { t, n ->
            val tagsNames = t.map { tagList -> tagList.map { tag -> tag.name } }
            n.mapIndexed { index, note ->
                noteItemStateFromNote(note=note, tags=tagsNames[index])
            }
        }
    }
}