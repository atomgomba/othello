package com.ekezet.othello.feature.gameboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.feature.gameboard.GameBoardAction.ContinueGame
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.GameBoardState
import com.ekezet.othello.feature.gameboard.gameBoardLoop
import com.ekezet.othello.feature.gameboard.ui.components.GameBoard
import com.ekezet.othello.feature.gameboard.ui.components.GamePiece
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
fun GameBoardView(
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    LoopWrapper({ gameBoardLoop(scope) }) { state ->
        GameBoardViewImpl(
            state,
            modifier,
        )
    }
}

@Composable
internal fun GameBoardScope.GameBoardViewImpl(
    state: GameBoardState,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GamePiece(
                disk = currentDisk,
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = CircleShape,
                    ),
            )

            Text("Turn: $currentTurn")

            Spacer(Modifier.weight(1F))

            Text("Opponent: $opponentName")
        }

        Spacer(Modifier.height(16.dp))

        GameBoard(
            board = board,
            showPositions = showBoardPositions,
            overlay = overlay,
            onCellClick = state.onCellClick,
        )

        Spacer(Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(onClick = onResetGameClick) {
                Text("Reset game")
            }

            Button(onClick = onToggleIndicatorsClick) {
                Text("Toggle indicators")
            }
        }
    }

    LaunchedEffect(nextMovePosition) {
        if (nextMovePosition != null) {
            // briefly show the current move before the next turn
            delay(300L)
            emit(ContinueGame)
        }
    }
}
