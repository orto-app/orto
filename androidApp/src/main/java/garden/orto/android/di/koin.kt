package garden.orto.android.di

import garden.orto.shared.features.detail.mvi.TagDetailViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { TagDetailViewModel() }
}