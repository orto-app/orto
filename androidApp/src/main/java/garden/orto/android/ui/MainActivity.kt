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
import garden.orto.shared.domain.ISettingsRepository
import garden.orto.shared.features.create.mvi.NoteShareSheetViewModel
import garden.orto.shared.features.detail.mvi.TagDetailContract
import garden.orto.shared.features.detail.mvi.TagDetailViewModel
import org.koin.android.ext.android.inject

@OptIn(ExperimentalCoilApi::class)
class MainActivity : BaseActivity() {
    private val settingsRepository: ISettingsRepository by inject()
    private val vmTagDetail: TagDetailViewModel by inject()
    private val vmNoteShareSheet: NoteShareSheetViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiHomeTagName = settingsRepository.getByKey<String>("ui.home").get()
        vmTagDetail.setEvent(TagDetailContract.Event.OnGetNotes(uiHomeTagName))

        // Start a coroutine in the lifecycle scope
        setContent {
            OrtoTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Navigation(
                        vmTagDetail = vmTagDetail,
                        vmNoteShareSheet = vmNoteShareSheet,
                        uiHomeTagName = uiHomeTagName
                    )
                }
            }
        }
    }

    override val vm: Array<IExecutorScope>
        get() = arrayOf(
            vmTagDetail,
            vmNoteShareSheet
        )
}