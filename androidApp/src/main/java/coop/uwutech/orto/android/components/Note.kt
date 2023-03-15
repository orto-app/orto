package coop.uwutech.orto.android.components

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.navigation.compose.rememberNavController
import org.koin.core.scope.get
import coop.uwutech.orto.android.viewmodels.NotesForTagViewModel

@Preview
@Composable
fun NoteCard() {
    Card(

        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(15.dp)
        ) {
            Text(
                buildAnnotatedString {
                    append("welcome to ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900, color = Color(0xFF4552B8))
                    ) {
                        append("Jetpack Compose Playground")
                    }
                }
            )
            Text(
                buildAnnotatedString {
                    append("Now you are in the ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.W900)) {
                        append("Card")
                    }
                    append(" section")
                }
            )
        }
    }
}


@Composable
fun NotesList(viewModel: NotesForTagViewModel) {
    val notes = viewModel.uiState.value

    LazyColumn {
        items(notes) { item ->
            // Compose UI code for each item
            // For example:
            Text(text = item)
        }
    }
}

@Composable
fun NotesScreen(viewModel: NotesForTagViewModel = get()) {
val navController = rememberNavController()
    NotesList(viewModel = viewModel)
}