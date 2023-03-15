package coop.uwutech.orto.shared.di

import coop.uwutech.orto.shared.cache.Database
import coop.uwutech.orto.shared.cache.createInMemorySqlDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object TestModules {
    val testDbModules = module {
        single { createInMemorySqlDriver() }
        singleOf(::Database)
    }
}