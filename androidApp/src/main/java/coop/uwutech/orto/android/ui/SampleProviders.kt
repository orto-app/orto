package coop.uwutech.orto.android.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import coop.uwutech.orto.android.viewmodels.Result
import coop.uwutech.orto.shared.cache.Note

class SampleNoteCardUiStateProvider : PreviewParameterProvider<Result.NoteCardUiState> {
    override val values = sequenceOf(
        Result.NoteCardUiState(Note("UUID1", "title1", "url1", "img1", "content1"), listOf("tag1", "tag2")),
        Result.NoteCardUiState(Note("UUID2", "title2", "url2", "img2", "content2"), listOf("tag3", "tag4"))
    )
}