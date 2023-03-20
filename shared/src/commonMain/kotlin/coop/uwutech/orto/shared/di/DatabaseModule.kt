package coop.uwutech.orto.shared.di

import coop.uwutech.orto.shared.cache.local.Database
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.TagRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object DatabaseModule {
    val databaseModule = module {
        singleOf(::Database)
//        single<CoroutineDispatcher> { Dispatchers.IO }
        singleOf(::NoteRepository)
        singleOf(::TagRepository)
    }
}
