package garden.orto.android.ui.features.detail.tag

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun ActionBar(
    name: String
) {
    TopAppBar(
        title = { Text(text = "#$name") }
    )
}