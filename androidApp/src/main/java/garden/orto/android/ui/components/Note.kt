package garden.orto.android.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import garden.orto.shared.domain.model.NoteState

@Composable
fun NoteItem(
    state: NoteState,
    onClick: () -> Unit
) {
    Card(
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(state.content)
            //Image()
            Row {
                for (tag in state.tags) {
                    TagLabel(tagName = tag)
                }
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL)
@Composable
fun PrevOnBoardingActionButton() {
    val onClick: () -> Unit = {}
    val state = NoteState(0L, "# content\n\nWith also some #tags", listOf("tag1", "tag2"))
    NoteItem(state, onClick)
}