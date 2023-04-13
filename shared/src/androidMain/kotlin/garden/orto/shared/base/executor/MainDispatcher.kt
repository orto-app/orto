package garden.orto.shared.base.executor

import garden.orto.shared.base.executor.IDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class MainDispatcher : IDispatcher {
    actual override val dispatcher: CoroutineDispatcher = Dispatchers.Main
}