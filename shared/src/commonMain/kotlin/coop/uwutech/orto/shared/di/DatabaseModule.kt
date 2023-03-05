package coop.uwutech.orto.shared.di

import coop.uwutech.orto.shared.cache.Database
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.TagRepository
import org.koin.dsl.module

object DatabaseModule {
    val databaseModule = module {
        single { Database(get()) }
        single { NoteRepository() }
        single { TagRepository() }
    }
}
