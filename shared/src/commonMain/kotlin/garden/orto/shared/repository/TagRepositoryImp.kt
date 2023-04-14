package garden.orto.shared.repository

import garden.orto.shared.domain.ITagRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagRepositoryImp :
    KoinComponent, ITagRepository {
    private val localDataImp: ILocalData by inject()

    override fun createTagChain(name: String) = localDataImp.createTagChain(name)

    override val allTags = localDataImp.getAllTagsAsFlow()

    override fun getTagsForBlock(id: Long) = localDataImp.getTagsForBlockAsFlow(id)
}