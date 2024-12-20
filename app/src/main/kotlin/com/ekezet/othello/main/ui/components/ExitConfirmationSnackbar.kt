package com.ekezet.othello.main.ui.components

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnCancelExitClicked
import org.koin.compose.koinInject

@Composable
internal fun MainState.ExitConfirmationSnackbar(
    hostState: SnackbarHostState,
    confirmExit: Boolean,
    context: Context = koinInject(),
) {
    LaunchedEffect(isExitMessageVisible, confirmExit) {
        if (!(isExitMessageVisible && confirmExit)) {
            if (!confirmExit) {
                // reset snackbar state if setting has changed
                emit(OnCancelExitClicked)
                hostState.currentSnackbarData?.dismiss()
            }
            return@LaunchedEffect
        }
        hostState.showSnackbar(
            message = context.getString(R.string.main__nav__confirm_exit),
            actionLabel = context.getString(R.string.common__cancel),
        )
        emit(OnCancelExitClicked)
    }
}
