package coop.uwutech.orto.android.components

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coop.uwutech.orto.android.ui.SampleNoteCardUiStateProvider
import coop.uwutech.orto.android.viewmodels.Result
import coop.uwutech.orto.android.viewmodels.NotesForTagViewModel
import org.koin.androidx.compose.koinViewModel


@Preview(name = "Preview1", device = Devices.PIXEL)
@Composable
fun NoteCard(@PreviewParameter(SampleNoteCardUiStateProvider::class) state: Result.NoteCardUiState) {
    Card(
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                    ) {
                        append(state.title)
                    }
                }
            )
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


@Composable
fun NotesList(notes: List<Result>) {
    LazyColumn {
        items(notes) { result ->
            when (result) {
                is Result.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp)
                )
                is Result.Error -> Text("Error")
                is Result.NoteCardUiState -> NoteCard(result)
            }
        }
    }
}

@Composable
fun NotesScreen(viewModel: NotesForTagViewModel = koinViewModel()) {
    val uiStateFlow by viewModel.uiState.collectAsState()
    NotesList(notes = uiStateFlow)
}