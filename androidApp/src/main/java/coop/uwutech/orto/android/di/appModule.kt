package coop.uwutech.orto.android.di

import coop.uwutech.orto.android.viewmodels.NotesForTagViewModel
import coop.uwutech.orto.shared.cache.NoteRepository
import coop.uwutech.orto.shared.cache.TagRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NoteRepository() }
    single { TagRepository() }
    viewModel { NotesForTagViewModel(get(), get()) }
}