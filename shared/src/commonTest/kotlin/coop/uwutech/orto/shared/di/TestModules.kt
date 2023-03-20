package coop.uwutech.orto.shared.di

import coop.uwutech.orto.shared.cache.local.LocalDataImp
import coop.uwutech.orto.shared.cache.local.createInMemorySqlDriver
import coop.uwutech.orto.shared.domain.INoteRepository
import coop.uwutech.orto.shared.domain.ITagRepository
import coop.uwutech.orto.shared.repository.ILocalData
import coop.uwutech.orto.shared.repository.NoteRepositoryImp
import coop.uwutech.orto.shared.repository.TagRepositoryImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object TestModules {
    val testDbModules = module {
        single { createInMemorySqlDriver() }
        singleOf(::LocalDataImp)
    }
}