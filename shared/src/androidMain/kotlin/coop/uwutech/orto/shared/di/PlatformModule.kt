package coop.uwutech.orto.shared.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import coop.uwutech.orto.AndroidPlatform
import coop.uwutech.orto.shared.base.executor.MainDispatcher
import coop.uwutech.orto.shared.cache.OrtoDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual object PlatformModule {
    actual val platformModule = module {
        single<SqlDriver> {
            AndroidSqliteDriver(
                OrtoDatabase.Schema,
                get(),
                "orto.db"
            )
        }
        singleOf(::AndroidPlatform)
        singleOf(::MainDispatcher)
    }
}