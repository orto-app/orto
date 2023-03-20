package coop.uwutech.orto.shared.cache

import coop.uwutech.orto.shared.cache.local.Database
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagRepository() :
    KoinComponent {
    private val database: Database by inject()

    fun insertTag(tag: Tag) = database.insertTag(tag)

    val allTags = database.getAllTagsAsFlow()

    fun getTags(note: Note) = database.getTagsAsFlow(note)
}