package coop.uwutech.orto.android

import android.app.Application
import coop.uwutech.orto.android.di.appModule
import coop.uwutech.orto.shared.di.DatabaseModule.databaseModule
import coop.uwutech.orto.shared.di.PlatformModule.platformModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OrtoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@OrtoApplication)
            // Load modules
            modules(
                appModule,
                platformModule,
                databaseModule
            )
        }
    }
}