package garden.orto.shared.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import garden.orto.AndroidPlatform
import garden.orto.shared.base.executor.IDispatcher
import garden.orto.shared.base.executor.MainDispatcher
import garden.orto.shared.cache.OrtoDatabase
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
        single<IDispatcher> { MainDispatcher() }
    }
}
