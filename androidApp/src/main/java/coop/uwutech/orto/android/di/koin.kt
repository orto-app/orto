package coop.uwutech.orto.android.di

import coop.uwutech.orto.shared.features.detail.mvi.TagDetailViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { TagDetailViewModel() }
}