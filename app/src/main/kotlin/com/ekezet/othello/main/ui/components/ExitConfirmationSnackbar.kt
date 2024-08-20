package com.ekezet.othello.main.ui.components

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnCancelExitClicked
import org.koin.compose.koinInject

@Composable
internal fun MainState.ExitConfirmationSnackbar(
    hostState: SnackbarHostState,
    settings: GameSettings,
    context: Context = koinInject(),
) {
    val confirmExit by remember { derivedStateOf { settings.confirmExit } }

    if (!confirmExit) {
        return
    }

    LaunchedEffect(isExitMessageVisible) {
        if (!(isExitMessageVisible)) {
            return@LaunchedEffect
        }
        hostState.showSnackbar(
            message = context.getString(R.string.main__nav__confirm_exit),
            actionLabel = context.getString(R.string.common__cancel),
        )
        emit(OnCancelExitClicked)
    }
}
