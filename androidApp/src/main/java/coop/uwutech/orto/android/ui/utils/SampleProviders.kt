package coop.uwutech.orto.android.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import coop.uwutech.orto.shared.cache.Note
import coop.uwutech.orto.shared.domain.model.NoteItemState

class SampleNoteCardProvider : PreviewParameterProvider<NoteItemState> {
    override val values = sequenceOf(
        NoteItemState(Note(0, "title1", "url1", "img1", "content1"), listOf("tag1", "tag2")),
        NoteItemState(Note(1, "title2", "url2", "img2", "content2"), listOf("tag3", "tag4"))
    )
}

class SampleTagLabelProvider : PreviewParameterProvider<String> {
    override val values = sequenceOf("tag1", "tag2", "tag3", "tag4")
}