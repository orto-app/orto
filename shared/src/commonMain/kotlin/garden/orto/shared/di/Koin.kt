package garden.orto.shared.di

import garden.orto.shared.cache.local.LocalDataImp
import garden.orto.shared.cache.local.adapters.localDateTimeAdapter
import garden.orto.shared.di.PlatformModule.platformModule
import garden.orto.shared.domain.IBlockRepository
import garden.orto.shared.domain.ITagRepository
import garden.orto.shared.domain.interactors.CreateNotesUseCase
import garden.orto.shared.domain.interactors.DeleteNotesUseCase
import garden.orto.shared.domain.interactors.GetNotesForTagUseCase
import garden.orto.shared.domain.model.mapper.NoteStateMapper
import garden.orto.shared.repository.ILocalData
import garden.orto.shared.repository.BlockRepositoryImp
import garden.orto.shared.repository.TagRepositoryImp
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
            mapperModule,
            platformModule
        )
    }

// IOS
fun initKoin() = initKoin {}

val repositoryModule = module {
    single { localDateTimeAdapter }
    single<ILocalData> { LocalDataImp(get()) }
    single<IBlockRepository> { BlockRepositoryImp() }
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
    factory { GetNotesForTagUseCase(get(), get(), get()) }
    factory { DeleteNotesUseCase(get()) }
    factory { CreateNotesUseCase(get(), get()) }
}

val dispatcherModule = module {
    factory { Dispatchers.Default }
}

val mapperModule = module {
    factory { NoteStateMapper() }
}
