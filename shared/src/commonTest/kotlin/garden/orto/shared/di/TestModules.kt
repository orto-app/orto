package garden.orto.shared.di

import garden.orto.shared.cache.local.LocalDataImp
import garden.orto.shared.cache.local.createInMemorySqlDriver
import garden.orto.shared.domain.INoteRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.repository.ILocalData
import garden.orto.shared.repository.NoteRepositoryImp
import garden.orto.shared.repository.TagRepositoryImp
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object TestModules {
    val testDbModules = module {
        single { createInMemorySqlDriver() }
        singleOf(::LocalDataImp)
    }
}