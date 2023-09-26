package com.ekezet.othello.feature.gameboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.feature.gameboard.GameBoardAction.ContinueGame
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.GameBoardState
import com.ekezet.othello.feature.gameboard.MOVE_DELAY_MILLIS
import com.ekezet.othello.feature.gameboard.gameBoardLoop
import com.ekezet.othello.feature.gameboard.numDark
import com.ekezet.othello.feature.gameboard.numLight
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
        GameBoardViewImpl(state, modifier)
    }
}

@Composable
internal fun GameBoardScope.GameBoardViewImpl(
    state: GameBoardState,
    modifier: Modifier = Modifier,
) = with(state) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        BoardHeader()

        GameBoard(
            board = board,
            showPositions = showBoardPositions,
            overlay = overlay,
            onCellClick = state.onCellClick,
        )

        BoardFooter()
    }

    LaunchedEffect(nextMovePosition) {
        if (nextMovePosition != null) {
            delay(MOVE_DELAY_MILLIS)
            emit(ContinueGame)
        }
    }
}

@Composable
private fun GameBoardState.BoardHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DiskImage(disk = currentDisk)

        Text("Turn: $currentTurn")

        Spacer(Modifier.weight(1F))

        Text("Vs: $opponentName")
    }
}

@Composable
private fun GameBoardState.BoardFooter() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DiskImage(disk = Disk.Dark)
            Text(diskCount.numDark.toString())

            Spacer(Modifier.weight(1F))

            Text(diskCount.numLight.toString())
            DiskImage(disk = Disk.Light)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(onClick = onResetGameClick) {
                Text("Reset game")
            }

            Button(onClick = onToggleIndicatorsClick) {
                Text("Toggle indicators")
            }
        }
    }
}

@Composable
private fun DiskImage(disk: Disk) {
    GamePiece(
        disk = disk,
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = CircleShape,
            ),
    )
}
