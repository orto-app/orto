package garden.orto.shared.domain.interactors

import garden.orto.shared.cache.Block
import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.domain.interactors.type.UseCaseInOutFlow
import garden.orto.shared.domain.model.NoteState
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
open class GetNotesForTagUseCase(
    private val blockRepository: IBlockRepository,
    private val tagRepository: ITagRepository,
    private val mapper: NoteStateMapper
) : UseCaseInOutFlow<String, List<NoteState>>() {
    override fun build(param: String): Flow<List<NoteState>> {
        val blocks: Flow<List<Block>> = blockRepository.getBlocksForTag(param)
        val tags: Flow<List<List<String>>> = blocks.flatMapLatest { blockList ->
            // Create a Flow of List<Tag> for each Note
            val tagFlowList = blockList.map { block ->
                tagRepository.getTagsForBlock(block.id)
                    // Exclude the current tag
                    .map { tags -> tags.filter { it.name != param } }
            }

            if (tagFlowList.isNotEmpty()) {
                // Combine all the List<Tag> flows into one Flow<List<List<String>>>
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
            } else {
                flowOf(emptyList())
            }
        }
        return blocks.zip(tags) { b, t ->
            b.mapIndexed { index, block ->
                mapper.inverseMap(block = block, tags = t[index])
            }
        }
    }
}