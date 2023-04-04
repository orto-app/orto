package garden.orto.android.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import garden.orto.android.ui.utils.SampleTagLabelProvider

@Preview(name = "TagLabel", device = Devices.PIXEL)
@Composable
fun TagLabel(@PreviewParameter(SampleTagLabelProvider::class) tagName: String) {
    Text(text = "#$tagName")
}