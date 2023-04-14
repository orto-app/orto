package garden.orto.shared.repository

import garden.orto.shared.cache.Block
import garden.orto.shared.cache.Tag
import kotlinx.coroutines.flow.Flow

interface ILocalData {
    fun clearDatabase()

    fun createTagChain(name: String): Tag

    fun getAllTagsAsFlow(): Flow<List<Tag>>

    fun getTagsForBlockAsFlow(blockId: Long): Flow<List<Tag>>

    fun deleteBlocks(blockIds: List<Long>)

    fun getAllBlocksAsFlow(): Flow<List<Block>>

    fun getBlocksForTagAsFlow(tagName: String): Flow<List<Block>>

    fun createNote(block: Block, tags: Collection<String>)
}