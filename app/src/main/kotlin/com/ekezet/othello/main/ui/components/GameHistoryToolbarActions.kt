package com.ekezet.othello.main.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnCopyHistoryClicked
import com.ekezet.othello.main.OnShowHistoryThumbnailsClicked
import com.ekezet.othello.main.OnShowTextHistoryClicked

@Composable
internal fun MainState.GameHistoryToolbarActions(
    showTextHistory: Boolean,
) {
    AnimatedContent(
        targetState = showTextHistory,
    ) { showAsText ->
        Row {
            if (showAsText) {
                TextActions()
            } else {
                ThumbnailsActions()
            }
        }
    }
}

@Composable
private fun MainState.ThumbnailsActions() {
    IconButton(onClick = { emit(OnShowTextHistoryClicked) }) {
        Icon(
            imageVector = Icons.Default.TextFields,
            contentDescription = stringResource(string.game_history__menu__text_view),
        )
    }
}

@Composable
private fun MainState.TextActions() {
    IconButton(onClick = { emit(OnShowHistoryThumbnailsClicked) }) {
        Icon(
            imageVector = Icons.Default.Image,
            contentDescription = stringResource(string.game_history__menu__thumbnail_view),
        )
    }

    IconButton(onClick = { emit(OnCopyHistoryClicked) }) {
        Icon(
            imageVector = Icons.Default.ContentCopy,
            contentDescription = stringResource(string.game_history__menu__copy_moves),
        )
    }

    /*
        IconButton(onClick = { emit(OnPasteHistoryClicked) }) {
            Icon(
                imageVector = Icons.Default.ContentPaste,
                contentDescription = stringResource(string.game_history__menu__paste_moves),
            )
        }
     */
}
