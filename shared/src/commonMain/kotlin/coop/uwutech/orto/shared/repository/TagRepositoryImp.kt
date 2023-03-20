package coop.uwutech.orto.shared.repository

import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.cache.Tag
import coop.uwutech.orto.shared.cache.local.LocalDataImp
import coop.uwutech.orto.shared.domain.ITagRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagRepositoryImp :
    KoinComponent, ITagRepository {
    private val localDataImp: ILocalData by inject()

    override fun insertTag(tag: Tag) = localDataImp.insertTag(tag)

    override val allTags = localDataImp.getAllTagsAsFlow()

    override fun getTagsForNote(id: Long) = localDataImp.getTagsForNoteAsFlow(id)
}