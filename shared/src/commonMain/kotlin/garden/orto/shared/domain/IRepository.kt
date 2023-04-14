package garden.orto.shared.domain

import garden.orto.shared.cache.Block
import garden.orto.shared.cache.Tag
import kotlinx.coroutines.flow.Flow

interface IBlockRepository {
    fun createNote(block: Block, tags: Collection<String>)
    fun deleteBlocks(blockIds: List<Long>)
    fun getAllBlocks(): Flow<List<Block>>
    fun getBlocksForTag(tagName: String): Flow<List<Block>>
}

interface ITagRepository {
    fun createTagChain(name: String): Tag
    val allTags: Flow<List<Tag>>
    fun getTagsForBlock(id: Long): Flow<List<Tag>>
}