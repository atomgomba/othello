package com.ekezet.othello.feature.gameboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.game.numDark
import com.ekezet.othello.core.game.numLight
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.gameboard.ACTION_DELAY_MILLIS
import com.ekezet.othello.feature.gameboard.GameBoardAction
import com.ekezet.othello.feature.gameboard.GameBoardAction.ContinueGame
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.GameBoardState
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.di.GameBoardScopeName
import com.ekezet.othello.feature.gameboard.ui.components.GameBoard
import com.ekezet.othello.feature.gameboard.ui.components.GamePiece
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import org.koin.compose.koinInject
import java.util.concurrent.TimeUnit.SECONDS

private val selectedColor: Color
    @Composable get() = MaterialTheme.colorScheme.inversePrimary

@Composable
fun GameBoardView(
    args: GameBoardArgs,
    modifier: Modifier = Modifier,
    loopScope: GameBoardScope = koinInject(GameBoardScopeName),
) {
    LoopWrapper<GameBoardState, GameBoardModel, GameBoardArgs, Unit, GameBoardAction>(
        builder = { loopScope },
        args = args,
    ) { state ->
        GameBoardViewImpl(state, modifier)
    }
}

@Composable
private fun GameBoardScope.GameBoardViewImpl(
    state: GameBoardState,
    modifier: Modifier = Modifier,
) = with(state) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BoardHeader()

            GameBoard(
                board = board,
                showPositions = showBoardPositions,
                ended = ended,
                overlay = overlay,
                onCellClick = state.onCellClick,
            )

            BoardFooter()
        }

        if (celebrate) {
            KonfettiView(
                modifier = Modifier
                    .matchParentSize()
                    .zIndex(Float.MAX_VALUE),
                parties = listOf(
                    Party(
                        emitter = Emitter(duration = 5, SECONDS).perSecond(30),
                    ),
                ),
            )
        }
    }

    if (nextMovePosition != null) {
        LaunchedEffect(nextMovePosition.x, nextMovePosition.y) {
            delay(ACTION_DELAY_MILLIS)
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
        Text(
            text = buildAnnotatedString {
                append(vs)
                addStyle(
                    SpanStyle(fontWeight = FontWeight.Bold),
                    vs.length - opponent.length,
                    vs.length,
                )
            },
        )
    }
}

@Composable
private fun GameBoardState.BoardFooter() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val isDarkWin = ended is EndedWin && ended.winner == Disk.Dark
        DiskImage(disk = Disk.Dark, isSelected = isDarkWin)
        Text("${diskCount.numDark}", color = if (isDarkWin) selectedColor else Color.Unspecified)

        Spacer(Modifier.weight(1F))

        val isLightWin = ended is EndedWin && ended.winner == Disk.Light
        Text("${diskCount.numLight}", color = if (isLightWin) selectedColor else Color.Unspecified)
        DiskImage(disk = Disk.Light, isSelected = isLightWin)
    }
}

@Composable
private fun DiskImage(disk: Disk, isSelected: Boolean = false) {
    GamePiece(
        disk = disk,
        modifier = Modifier
            .size(24.dp)
            .border(
                width = 1.dp,
                color = if (!isSelected) {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = .666F)
                } else {
                    selectedColor
                },
                shape = CircleShape,
            ),
    )
}
