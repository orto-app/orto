package coop.uwutech.orto.android.ui.components

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
import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.domain.model.NoteItemState

@Composable
fun NoteItem(
    state: NoteItemState, onClick: () -> Unit
) {
    Card(
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                ) {
                    append(state.title)
                }
            })
            state.content?.let { Text(it) }
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
    val state = NoteItemState(Note(0, "title1", "url1", "img1", "content1"), listOf("tag1", "tag2"))
    NoteItem(state, onClick)
}