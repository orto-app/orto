package coop.uwutech.orto.shared.di

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import coop.uwutech.orto.IOSPlatform
import coop.uwutech.orto.Platform
import coop.uwutech.orto.shared.base.executor.MainDispatcher
import coop.uwutech.orto.shared.cache.OrtoDatabase
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

actual object PlatformModule {
    actual val platformModule = module {
        single<SqlDriver> { NativeSqliteDriver(OrtoDatabase.Schema, "orto.db") }
        single<Platform> { IOSPlatform() }
        single { MainDispatcher() }
    }
}