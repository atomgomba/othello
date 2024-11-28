package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.components.GamePieceWithBorder

@Composable
internal fun GameEndItemView(
    gameEnd: GameEnd,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (gameEnd) {
            GameEnd.EndedTie -> EndedTieView()
            is GameEnd.EndedWin -> EndedWinView(gameEnd)
        }
    }
}

@Composable
private fun EndedTieView() {
    Column(
        modifier = Modifier.height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(
                id = R.string.game_history__list__ended_tie,
            ),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

@Composable
private fun EndedWinView(gameEnd: GameEnd.EndedWin) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        GamePieceWithBorder(
            disk = gameEnd.winner,
            modifier = Modifier.height(IntrinsicSize.Max),
        )

        Text(
            text = stringResource(
                id = R.string.game_history__list__ended_win,
            ),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}
