package coop.uwutech.orto.android.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import coop.uwutech.orto.android.ui.SampleTagLabelProvider

@Preview(name = "TagLabel", device = Devices.PIXEL)
@Composable
fun TagLabel(@PreviewParameter(SampleTagLabelProvider::class) tagName: String) {
    Text(text = "#$tagName")
}