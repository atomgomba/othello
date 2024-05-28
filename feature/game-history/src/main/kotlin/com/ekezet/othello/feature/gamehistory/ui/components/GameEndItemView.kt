package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.stringResource

@Composable
internal fun GameEndItemView(gameEnd: GameEnd, lastState: OthelloGameState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        when (gameEnd) {
            GameEnd.EndedTie -> EndedTieView(lastState)
            is GameEnd.EndedWin -> EndedWinView(gameEnd, lastState)
        }
    }
}

@Composable
private fun EndedTieView(lastState: OthelloGameState) {
    Text(
        text = stringResource(
            id = R.string.game_history__list__ended_tie,
            lastState.diskCount.first,
        ),
        style = MaterialTheme.typography.headlineMedium,
    )
}

@Composable
private fun EndedWinView(gameEnd: GameEnd.EndedWin, lastState: OthelloGameState) {
    Text(
        text = stringResource(
            id = R.string.game_history__list__ended_win,
            gameEnd.winner.stringResource,
            lastState.diskCount.first,
            lastState.diskCount.second,
        ),
        style = MaterialTheme.typography.headlineMedium,
    )
}
