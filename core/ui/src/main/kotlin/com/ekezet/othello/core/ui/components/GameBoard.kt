package com.ekezet.othello.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.data.serialize.PositionLetters
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameEnd.EndedTie
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.ui.theme.BoardBackground
import com.ekezet.othello.core.ui.viewModels.BoardList
import com.ekezet.othello.core.ui.viewModels.Sprite
import com.ekezet.othello.core.ui.viewModels.getAt

private val borderWidth = 1.dp
private const val LOSER_ALPHA = 1 / 3F
private const val CELL_WEIGHT = 1F / BoardWidth
private val positionSize = 24.dp

@ExperimentalLayoutApi
@Composable
fun GameBoard(
    board: BoardList,
    modifier: Modifier = Modifier,
    background: Color = BoardBackground,
    boardCornerRadius: Dp = 16.dp,
    showPositions: Boolean = false,
    nextMovePosition: Position? = null,
    ended: GameEnd? = null,
    overlayFactory: (colIndex: Int, rowIndex: Int) -> Sprite? = { _, _ -> null },
    isClickable: Boolean = true,
    onCellClick: (x: Int, y: Int) -> Unit = { _, _ -> },
) {
    Column(modifier = modifier) {
        Row {
            GameBoardImpl(
                board = board,
                background = background,
                boardCornerRadius = boardCornerRadius,
                showPositions = showPositions,
                nextMovePosition = nextMovePosition,
                ended = ended,
                overlayFactory = overlayFactory,
                isClickable = isClickable,
                onCellClick = onCellClick,
            )
        }
    }
}

@ExperimentalLayoutApi
@Composable
private fun GameBoardImpl(
    board: BoardList,
    background: Color,
    boardCornerRadius: Dp,
    showPositions: Boolean,
    nextMovePosition: Position?,
    ended: GameEnd?,
    overlayFactory: (colIndex: Int, rowIndex: Int) -> Sprite?,
    isClickable: Boolean,
    onCellClick: (x: Int, y: Int) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(borderWidth),
    ) {
        for (rowIndex in 0 until BoardHeight) {
            if (showPositions && rowIndex == 0) {
                HorizontalPositions(nextMovePosition = nextMovePosition)
            }

            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(borderWidth),
            ) {
                for (colIndex in 0 until BoardWidth) {
                    val disk = board.getAt(colIndex, rowIndex)
                    val overlayItem = overlayFactory(colIndex, rowIndex)

                    if (showPositions && colIndex == 0) {
                        VerticalPosition(number = rowIndex + 1, nextMovePosition = nextMovePosition)
                    }

                    key(colIndex, rowIndex, disk, overlayItem?.composeKey, ended) {
                        Box(
                            modifier = Modifier
                                .roundedCell(colIndex, rowIndex, boardCornerRadius)
                                .background(color = background)
                                .weight(CELL_WEIGHT)
                                .aspectRatio(1F)
                                .clickable(enabled = isClickable) {
                                    onCellClick(colIndex, rowIndex)
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            if (disk != null) {
                                val alpha: Float by animateFloatAsState(
                                    label = "board-disk-alpha",
                                    targetValue = when (ended) {
                                        null -> 1F
                                        is EndedWin -> if (ended.winner == disk) 1F else LOSER_ALPHA
                                        EndedTie -> LOSER_ALPHA
                                    },
                                )

                                GamePiece(
                                    disk = disk,
                                    modifier = Modifier.alpha(alpha),
                                )
                            }
                            overlayItem?.Composable()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalPositions(
    nextMovePosition: Position?,
) {
    Row(
        modifier = Modifier
            .padding(start = positionSize)
            .fillMaxWidth()
            .height(positionSize),
    ) {
        for ((i, letter) in PositionLetters.withIndex()) {
            Text(
                text = "$letter",
                textAlign = TextAlign.Center,
                color = if (nextMovePosition?.x == i) Color.Unspecified else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.weight(CELL_WEIGHT),
            )
        }
    }
}

@ExperimentalLayoutApi
@Composable
private fun VerticalPosition(
    number: Int,
    nextMovePosition: Position?,
) {
    Column {
        Box {
            Text(
                text = "$number",
                textAlign = TextAlign.Center,
                color = if (nextMovePosition?.y == number - 1) Color.Unspecified else MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .width(positionSize)
                    .align(Alignment.Center),
            )
        }
    }
}

private fun Modifier.roundedCell(x: Int, y: Int, radius: Dp) = if (x == 0 && y == 0) {
    clip(shape = RoundedCornerShape(topStart = radius))
} else if (x == 7 && y == 0) {
    clip(shape = RoundedCornerShape(topEnd = radius))
} else if (x == 0 && y == 7) {
    clip(shape = RoundedCornerShape(bottomStart = radius))
} else if (x == 7 && y == 7) {
    clip(shape = RoundedCornerShape(bottomEnd = radius))
} else {
    this
}
