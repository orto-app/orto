package coop.uwutech.orto.android.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import coil.annotation.ExperimentalCoilApi
import coop.uwutech.orto.android.OrtoTheme
import coop.uwutech.orto.android.base.BaseActivity
import coop.uwutech.orto.android.ui.navigation.Navigation
import coop.uwutech.orto.shared.base.executor.IExecutorScope
import coop.uwutech.orto.shared.features.detail.mvi.TagDetailViewModel
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