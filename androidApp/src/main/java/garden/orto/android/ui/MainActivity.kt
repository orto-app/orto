package garden.orto.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import coil.annotation.ExperimentalCoilApi
import garden.orto.android.OrtoTheme
import garden.orto.android.base.BaseActivity
import garden.orto.android.ui.navigation.Navigation
import garden.orto.shared.base.executor.IExecutorScope
import garden.orto.shared.features.detail.mvi.TagDetailViewModel
import org.koin.android.ext.android.inject

@OptIn(ExperimentalCoilApi::class)
class MainActivity : BaseActivity() {
    private val vmTagDetail: TagDetailViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Orto"
        // Start a coroutine in the lifecycle scope
        setContent {
            OrtoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(
                        vmTagDetail = vmTagDetail
                    )
                }
            }
        }
    }

    override val vm: Array<IExecutorScope>
        get() = arrayOf(vmTagDetail)
}