package garden.orto.shared.di

import garden.orto.shared.cache.local.LocalDataImp
import garden.orto.shared.cache.local.createInMemorySqlDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object TestModules {
    val testDbModules = module {
        single { createInMemorySqlDriver() }
        singleOf(::LocalDataImp)
    }
}