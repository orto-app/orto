package garden.orto.shared.base.executor

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class MainIOExecutor : IExecutorScope, CoroutineScope, KoinComponent {

    private val mainDispatcher: IDispatcher by inject()
    private val ioDispatcher: CoroutineDispatcher by inject()

    private val job: CompletableJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + mainDispatcher.dispatcher

    override fun cancel() {
        job.cancel()
    }

    protected fun <T> collect(
        flow: Flow<T>, collect: (T) -> Unit
    ) {
        launch {
            flow
                .flowOn(ioDispatcher)
                .collect {
                    collect(it)
                }
        }
    }

    protected fun <T> collectLatest(
        flow: Flow<T>, collect: (T) -> Unit
    ) {
        launch {
            flow
                .flowOn(ioDispatcher)
                .collectLatest {
                    collect(it)
                }
        }
    }
}