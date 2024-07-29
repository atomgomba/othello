package com.ekezet.othello.feature.gameboard.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.data.models.numDark
import com.ekezet.othello.core.data.models.numLight
import com.ekezet.othello.core.game.GameEnd.EndedTie
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.ui.R.string
import com.ekezet.othello.core.ui.components.GameBoard
import com.ekezet.othello.core.ui.components.GamePiece
import com.ekezet.othello.core.ui.orHumanPlayer
import com.ekezet.othello.feature.gameboard.ACTION_DELAY_MILLIS
import com.ekezet.othello.feature.gameboard.GameBoardLoop
import com.ekezet.othello.feature.gameboard.GameBoardState
import com.ekezet.othello.feature.gameboard.actions.ContinueGame
import com.ekezet.othello.feature.gameboard.actions.OnMoveMade
import com.ekezet.othello.feature.gameboard.actions.OnStrategyClick
import com.ekezet.othello.feature.gameboard.ui.viewModels.getAt
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit.SECONDS

private val highlightColor: Color
    @Composable get() = MaterialTheme.colorScheme.tertiary

@ExperimentalLayoutApi
@Composable
fun GameBoardView(
    args: GameSettings,
    parentEmitter: AnyActionEmitter?,
    onStrategyClick: OnStrategyClick,
    modifier: Modifier = Modifier,
) {
    LoopWrapper(
        builder = GameBoardLoop,
        args = args,
        parentEmitter = parentEmitter,
    ) {
        GameBoardViewImpl(onStrategyClick, modifier)
    }
}

@ExperimentalLayoutApi
@Composable
private fun GameBoardState.GameBoardViewImpl(
    onStrategyClick: OnStrategyClick,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.then(Modifier.padding(16.dp)),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Center),
        ) {
            item { BoardHeader() }

            item {
                GameBoard(
                    board = board,
                    background = boardBackground,
                    showPositions = displayOptions.showBoardPositions,
                    nextMovePosition = nextMovePosition,
                    ended = ended,
                    overlayFactory = { colIndex, rowIndex -> overlay.getAt(colIndex, rowIndex) },
                    isClickable = isHumanPlayer,
                    onCellClick = { x, y -> emit(OnMoveMade(Position(x, y))) },
                )
            }

            item { BoardFooter(onStrategyClick) }
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
private fun GameBoardState.BoardFooter(
    onStrategyClick: OnStrategyClick,
) {
    val isDarkWin = ended is EndedWin && ended.winner.isDark
    val isLightWin = ended is EndedWin && ended.winner.isLight

    Column {
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

    Spacer(Modifier.height(8.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(onClick = { onStrategyClick(Disk.Dark) }) {
            Text(
                text = darkStrategyName.orHumanPlayer,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isDarkWin) highlightColor else Color.Unspecified,
            )
        }

        Spacer(Modifier.weight(1F))

        OutlinedButton(onClick = { onStrategyClick(Disk.Light) }) {
            Text(
                text = lightStrategyName.orHumanPlayer,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isLightWin) highlightColor else Color.Unspecified,
            )
        }
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
