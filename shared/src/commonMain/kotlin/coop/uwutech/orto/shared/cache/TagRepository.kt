package coop.uwutech.orto.shared.cache

import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TagRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default) :
    KoinComponent {
    private val scope = CoroutineScope(ioDispatcher)
    private val database: Database by inject()

    fun initialize() = scope.async {
        // ...
    }

    fun insertTag(tag: Tag) = database.insertTag(tag)

    val allTags = database.getAllTagsAsFlow()

    fun getTags(note: Note) = database.getTagsAsFlow(note)
}