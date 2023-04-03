package coop.uwutech.orto.shared.domain.interactors

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import coop.uwutech.orto.shared.domain.INoteRepository
import coop.uwutech.orto.shared.domain.ITagRepository
import coop.uwutech.orto.shared.domain.interactors.type.UseCaseInOutFlow
import coop.uwutech.orto.shared.domain.model.NoteItemState
import coop.uwutech.orto.shared.domain.model.noteItemStateFromNote
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetNotesForTagUseCase(
    private val noteRepository: INoteRepository,
    private val tagRepository: ITagRepository
) : UseCaseInOutFlow<String, List<NoteItemState>>() {
    override fun build(param: String): Flow<List<NoteItemState>> {
        val notes: Flow<List<Note>> = noteRepository.getNotesForTag(param)
        val tags: Flow<List<List<String>>> = notes.flatMapLatest { noteList ->
            // Create a Flow of List<Tag> for each Note
            val tagFlowList = noteList.map { note ->
                tagRepository.getTagsForNote(note.id)
                    // Exclude the current tag
                    .map { tags -> tags.filter { it.name != param } }
            }

            // Combine all the List<Tag> flows into one Flow<List<List<Tag>>>
            combine(tagFlowList) { tagListArray ->
                val frequencies = tagListArray.toList()
                    .flatten()
                    .groupingBy { it.id }
                    .eachCount()
                tagListArray.map { tags ->
                    // Sort tags by relative frequency to the input tag
                    tags.sortedWith { a, b ->
                        val aFrequency = frequencies[a.id]!!
                        val bFrequency = frequencies[b.id]!!
                        if (aFrequency > bFrequency) {
                            -1
                        } else if (aFrequency < bFrequency) {
                            1
                        } else {
                            0
                        }
                    }.map { tag -> tag.name }
                }
            }
        }
        return tags.zip(notes) { t, n ->
            n.mapIndexed { index, note ->
                noteItemStateFromNote(note = note, tags = t[index])
            }
        }
    }
}