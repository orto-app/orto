package garden.orto.shared.base.executor

import kotlinx.coroutines.CoroutineDispatcher

expect class MainDispatcher() : IDispatcher {
    override val dispatcher: CoroutineDispatcher
}