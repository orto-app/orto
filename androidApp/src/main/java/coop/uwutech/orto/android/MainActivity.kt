package coop.uwutech.orto.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coop.uwutech.orto.android.components.NotesScreen
import coop.uwutech.orto.android.viewmodels.NotesForTagUiState
import coop.uwutech.orto.android.viewmodels.NotesForTagViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val notesForTagViewModel: NotesForTagViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Orto"
        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                NotesScreen()
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                notesForTagViewModel.uiState.collect { uiState ->
                    // New value received
                    when (uiState) {
                        is NotesForTagUiState.Loading ->
                        is NotesForTagUiState.NoteCardUiState -> showFavoriteNews(uiState.news)
                        is NotesForTagUiState.Error -> showError(uiState.exception)
                    }
                }
            }
        }
    }
}