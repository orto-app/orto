package garden.orto.shared.cache.local

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import garden.orto.shared.cache.Block
import garden.orto.shared.cache.OrtoDatabase
import garden.orto.shared.cache.Tag
import garden.orto.shared.cache.local.adapters.localDateTimeAdapter
import garden.orto.shared.repository.ILocalData
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent

internal class LocalDataImp(
    databaseDriver: SqlDriver
) : KoinComponent, ILocalData {
    private val database = OrtoDatabase(
        driver = databaseDriver,
        BlockAdapter = Block.Adapter(
            created_atAdapter = localDateTimeAdapter,
            updated_atAdapter = localDateTimeAdapter
        ),
        TagAdapter = Tag.Adapter(
            created_atAdapter = localDateTimeAdapter,
            updated_atAdapter = localDateTimeAdapter
        )
    )
    private val dbQuery = database.ortoDatabaseQueries

    /**
     * General
     */

    override fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllRelations()
            dbQuery.removeAllTags()
            dbQuery.removeAllBlocks()
        }
    }

    /**
     * Tags
     */

    private fun getOrCreateTag(name: String, parent_id: Long?): Tag {
        val tag = dbQuery.getTagByName(name).executeAsOneOrNull()
        if (tag == null) {
            dbQuery.insertTag(name = name, parent_id = parent_id)
        }
        return dbQuery.getTagByName(name).executeAsOne()
    }

    override fun createTagChain(name: String): Tag = dbQuery.transactionWithResult {
        var parentName = ""
        name.split("/").forEach { partialName ->
            parentName = getOrCreateTag(
                listOf(parentName, partialName).filter { it.isNotBlank() }.joinToString("/"),
                dbQuery.getTagIdByName(parentName).executeAsOneOrNull()
            ).name
        }
        return@transactionWithResult dbQuery.getTagByName(name).executeAsOne()
    }

    private fun _getAllTags() = dbQuery.getAllTags()
    fun getAllTags(): List<Tag> = _getAllTags().executeAsList()
    override fun getAllTagsAsFlow(): Flow<List<Tag>> =
        _getAllTags()
            .asFlow()
            .mapToList()

    private fun _getTagsForBlock(blockId: Long) = dbQuery.getTagsForBlock(block_id = blockId)
    fun getTagsForBlock(blockId: Long): List<Tag> = _getTagsForBlock(blockId).executeAsList()
    override fun getTagsForBlockAsFlow(blockId: Long): Flow<List<Tag>> =
        _getTagsForBlock(blockId)
            .asFlow()
            .mapToList()

    /**
     * Blocks
     */

    private fun insertBlock(block: Block) {
        dbQuery.insertBlock(
            content = block.content
        )
    }

    override fun deleteBlocks(blockIds: List<Long>) {
        dbQuery.transaction {
            blockIds.forEach {
                dbQuery.deleteBlock(
                    id = it
                )
                dbQuery.deleteBlockRelations(block_id = it)
            }
        }
    }

    private fun _getAllBlocks() = dbQuery.getAllBlocks()
    fun getAllBlocks() = _getAllBlocks().executeAsList()
    override fun getAllBlocksAsFlow(): Flow<List<Block>> =
        _getAllBlocks()
            .asFlow()
            .mapToList()

    private fun _getBlocksForTag(name: String): Query<Block> = dbQuery.getBlocksForTag(name = name)
    fun getBlocksForTag(name: String): List<Block> = _getBlocksForTag(name = name).executeAsList()
    override fun getBlocksForTagAsFlow(tagName: String): Flow<List<Block>> =
        _getBlocksForTag(tagName)
            .asFlow()
            .mapToList()

    /**
     * Blocks
     */

    override fun createNote(block: Block, tags: Iterable<String>) {
        dbQuery.transaction {
            insertBlock(block)
            tags.forEach {
                val tag = createTagChain(it)
                dbQuery.insertBlockTagRelation(
                    block.id,
                    tag.id
                )
            }
        }
    }

    override fun createNotes(blocks: Iterable<Pair<Block, Iterable<String>>>) {
        dbQuery.transaction {
            blocks.forEach {
                createNote(it.first, it.second)
            }
        }
    }
}