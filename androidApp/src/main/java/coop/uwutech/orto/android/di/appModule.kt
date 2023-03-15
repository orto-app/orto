package coop.uwutech.orto.android.di

import coop.uwutech.orto.android.viewmodels.NotesForTagViewModel
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.TagRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::NoteRepository)
    singleOf(::TagRepository)
    viewModelOf(::NotesForTagViewModel)
}