package com.ekezet.othello.feature.gameboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.gameboard.GameBoardAction.ContinueGame
import com.ekezet.othello.feature.gameboard.GameBoardArgs
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
    args: GameBoardArgs,
    modifier: Modifier = Modifier,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    LoopWrapper({ gameBoardLoop(scope, args) }, args) { state ->
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

        val turn = stringResource(id = string.game_board__header__turn, currentTurn)
        Text(text = turn)

        Spacer(Modifier.weight(1F))

        val opponent = opponentName ?: stringResource(string.game_board__header__human)
        val vs = stringResource(string.game_board__header__vs, opponent)
        Text(text = buildAnnotatedString {
            append(vs)
            addStyle(SpanStyle(fontWeight = FontWeight.Bold), vs.length - opponent.length, vs.length)
        })
    }
}

@Composable
private fun GameBoardState.BoardFooter() {
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
