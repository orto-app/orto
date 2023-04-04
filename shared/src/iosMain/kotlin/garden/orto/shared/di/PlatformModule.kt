package garden.orto.shared.di

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import garden.orto.IOSPlatform
import garden.orto.Platform
import garden.orto.shared.base.executor.MainDispatcher
import garden.orto.shared.cache.OrtoDatabase
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

actual object PlatformModule {
    actual val platformModule = module {
        single<SqlDriver> { NativeSqliteDriver(OrtoDatabase.Schema, "orto.db") }
        single<Platform> { IOSPlatform() }
        single { MainDispatcher() }
    }
}