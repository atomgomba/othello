package com.ekezet.othello.main.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ekezet.othello.core.ui.R.drawable
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.main.MainState
import com.ekezet.othello.main.OnNewGameClicked
import com.ekezet.othello.main.OnToggleIndicatorsClicked

@Composable
internal fun MainState.GameBoardToolbarActions(
    showPossibleMoves: Boolean,
) {
    Row {
        IconButton(onClick = { emit(OnNewGameClicked) }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(string.main__menu__new_game),
            )
        }

        IconToggleButton(
            checked = showPossibleMoves,
            onCheckedChange = { emit(OnToggleIndicatorsClicked) },
        ) {
            Icon(
                painter = painterResource(
                    id = if (showPossibleMoves) drawable.ic_visibility else drawable.ic_visibility_off,
                ),
                contentDescription = stringResource(string.main__menu__toggle_indicators),
            )
        }
    }
}
