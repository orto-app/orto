package garden.orto.android.di

import android.app.Application
import android.content.SharedPreferences
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import garden.orto.shared.domain.ISettingsRepository
import garden.orto.shared.features.detail.mvi.TagDetailViewModel
import garden.orto.shared.repository.SettingRepositoryImp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val viewModelModule = module {
    single { TagDetailViewModel() }
}

val settingsModule = module {
    single {
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

    single<Settings> { SharedPreferencesSettings(get()) }

    single<ISettingsRepository> { SettingRepositoryImp(get()) }
}

fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences("default", android.content.Context.MODE_PRIVATE)
}