package garden.orto.shared.repository

import garden.orto.shared.cache.Tag
import garden.orto.shared.domain.ITagRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagRepositoryImp :
    KoinComponent, ITagRepository {
    private val localDataImp: ILocalData by inject()

    override fun insertTag(tag: Tag) = localDataImp.insertTag(tag)

    override val allTags = localDataImp.getAllTagsAsFlow()

    override fun getTagsForNote(id: Long) = localDataImp.getTagsForNoteAsFlow(id)
}