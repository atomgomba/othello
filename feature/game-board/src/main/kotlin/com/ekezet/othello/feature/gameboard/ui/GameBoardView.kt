package com.ekezet.othello.feature.gameboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ekezet.hurok.AnyLoopScope
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.numDark
import com.ekezet.othello.core.data.models.numLight
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.feature.gameboard.ACTION_DELAY_MILLIS
import com.ekezet.othello.feature.gameboard.GameBoardLoop
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.GameBoardState
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.actions.ContinueGame
import com.ekezet.othello.feature.gameboard.ui.components.GameBoard
import com.ekezet.othello.feature.gameboard.ui.components.GamePiece
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit.SECONDS

private val highlightColor: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary

@Composable
fun GameBoardView(
    args: GameSettings,
    modifier: Modifier = Modifier,
    parentLoop: AnyLoopScope? = null,
) {
    LoopWrapper(
        builder = GameBoardLoop,
        parentLoop = parentLoop,
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
            modifier = Modifier.align(Center),
        ) {
            BoardHeader()

            GameBoard(
                board = board,
                showPositions = showBoardPositions,
                ended = ended,
                overlay = overlay,
                isClickable = isHumanPlayer,
                onCellClick = onCellClick,
            )

            BoardFooter()
        }

        if (celebrate) {
            Celebrate()
        }
    }

    LaunchedEffect(nextMovePosition != null) {
        if (nextMovePosition != null) {
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

        Text(text = stringResource(id = string.game_board__header__turn, currentTurn))

        if (passed) {
            Spacer(Modifier.weight(1F))
            Text(text = stringResource(id = string.game_board__header__passed))
        }
    }
}

@Composable
private fun GameBoardState.BoardFooter() {
    val isDarkWin = ended is EndedWin && ended.winner == Disk.Dark
    val isLightWin = ended is EndedWin && ended.winner == Disk.Light

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DiskImage(disk = Disk.Dark, isSelected = isDarkWin)
            Text(
                "${diskCount.numDark}",
                color = if (isDarkWin) highlightColor else Color.Unspecified,
            )

            Spacer(Modifier.weight(1F))

            if (ended != null) {
                Text(
                    text = stringResource(
                        id = when (ended) {
                            EndedTie -> string.game_board__footer__tie_game
                            is EndedWin -> string.game_board__footer__winner
                        },
                    ),
                    color = highlightColor,
                    fontWeight = FontWeight.Bold,
                )

                Spacer(Modifier.weight(1F))
            }

            Text(
                "${diskCount.numLight}",
                color = if (isLightWin) highlightColor else Color.Unspecified,
            )
            DiskImage(disk = Disk.Light, isSelected = isLightWin)
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = darkStrategyName ?: stringResource(string.common__human_player),
            fontWeight = FontWeight.Bold,
            color = if (isDarkWin) highlightColor else Color.Unspecified,
        )

        Spacer(Modifier.weight(1F))

        Text(
            text = lightStrategyName ?: stringResource(string.common__human_player),
            fontWeight = FontWeight.Bold,
            color = if (isLightWin) highlightColor else Color.Unspecified,
        )
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
                    MaterialTheme.colorScheme.outline
                } else {
                    highlightColor
                },
                shape = CircleShape,
            ),
    )
}

@Composable
private fun BoxScope.Celebrate() {
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
