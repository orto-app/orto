package coop.uwutech.orto.android.ui.features

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun ActionBar(
    name: String
) {
    TopAppBar(
        title = { Text(text = name) }
    )
}