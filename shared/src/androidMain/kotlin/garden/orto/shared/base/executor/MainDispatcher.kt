package garden.orto.shared.base.executor

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class MainDispatcher : IDispatcher {
    actual override val dispatcher: CoroutineDispatcher = Dispatchers.Main
}