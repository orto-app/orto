package garden.orto.shared.repository

import garden.orto.shared.cache.Block
import garden.orto.shared.domain.IBlockRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BlockRepositoryImp :
    KoinComponent, IBlockRepository {
    private val localData: ILocalData by inject()
    override fun createNote(block: Block, tags: Collection<String>) = localData.createNote(block, tags)
    override fun createNotes(blocks: Iterable<Pair<Block, Iterable<String>>>) = localData.createNotes(blocks)
    override fun deleteBlocks(blockIds: List<Long>) = localData.deleteBlocks(blockIds)
    override fun getAllBlocks(): Flow<List<Block>> = localData.getAllBlocksAsFlow()
    override fun getBlocksForTag(tagName: String) = localData.getBlocksForTagAsFlow(tagName)
}