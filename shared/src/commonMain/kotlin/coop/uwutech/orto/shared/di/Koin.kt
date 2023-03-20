package coop.uwutech.orto.shared.di

import coop.uwutech.orto.shared.cache.local.LocalDataImp
import coop.uwutech.orto.shared.di.PlatformModule.platformModule
import coop.uwutech.orto.shared.domain.INoteRepository
import coop.uwutech.orto.shared.domain.ITagRepository
import coop.uwutech.orto.shared.domain.interactors.GetNotesForTagUseCase
import coop.uwutech.orto.shared.repository.ILocalData
import coop.uwutech.orto.shared.repository.NoteRepositoryImp
import coop.uwutech.orto.shared.repository.TagRepositoryImp
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            repositoryModule,
            dispatcherModule,
            useCasesModule,
            platformModule
        )
    }

// IOS
fun initKoin() = initKoin {}

val repositoryModule = module {
    single<ILocalData> { LocalDataImp(get()) }
    single<INoteRepository> { NoteRepositoryImp() }
    single<ITagRepository> { TagRepositoryImp() }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }
}

val useCasesModule: Module = module {
    factory { GetNotesForTagUseCase(get(), get()) }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}