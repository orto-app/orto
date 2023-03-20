package coop.uwutech.orto.android.base

import androidx.activity.ComponentActivity
import coop.uwutech.orto.shared.base.executor.IExecutorScope

abstract class BaseActivity : ComponentActivity() {
    protected abstract val vm: Array<IExecutorScope>

    override fun onDestroy() {
        vm.forEach { it.cancel() }
        super.onDestroy()
    }
}