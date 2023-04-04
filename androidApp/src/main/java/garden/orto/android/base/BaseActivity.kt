package garden.orto.android.base

import androidx.activity.ComponentActivity
import garden.orto.shared.base.executor.IExecutorScope

abstract class BaseActivity : ComponentActivity() {
    protected abstract val vm: Array<IExecutorScope>

    override fun onDestroy() {
        vm.forEach { it.cancel() }
        super.onDestroy()
    }
}