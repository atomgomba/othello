package com.ekezet.othello.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.data.serialize.PositionLetters
import com.ekezet.othello.core.data.serialize.PositionNumbers
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameEnd.EndedTie
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.ui.theme.BoardBackground
import com.ekezet.othello.core.ui.viewModels.BoardList
import com.ekezet.othello.core.ui.viewModels.Sprite
import com.ekezet.othello.core.ui.viewModels.getAt

private const val LOSER_ALPHA = 1 / 3F
private const val CELL_WEIGHT = 1F / BoardWidth

@ExperimentalLayoutApi
@Composable
fun GameBoard(
    board: BoardList,
    modifier: Modifier = Modifier,
    background: Color = BoardBackground,
    gaps: Color = MaterialTheme.colorScheme.background,
    boardCornerRadius: Dp = 16.dp,
    showPositions: Boolean = false,
    nextMovePosition: Position? = null,
    ended: GameEnd? = null,
    overlayFactory: (colIndex: Int, rowIndex: Int) -> Sprite? = { _, _ -> null },
    isClickable: Boolean = true,
    onCellClick: (position: Position) -> Unit = {},
) {
    Box(modifier = modifier) {
        GameBoardImpl(
            board = board,
            background = background,
            gaps = gaps,
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

@ExperimentalLayoutApi
@Composable
private fun GameBoardImpl(
    board: BoardList,
    background: Color,
    gaps: Color,
    boardCornerRadius: Dp,
    showPositions: Boolean,
    nextMovePosition: Position?,
    ended: GameEnd?,
    overlayFactory: (colIndex: Int, rowIndex: Int) -> Sprite?,
    isClickable: Boolean,
    onCellClick: (position: Position) -> Unit,
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
) {
    Box(
        modifier = Modifier.run {
            if (showPositions) {
                drawPositions(
                    textMeasurer = textMeasurer,
                    color = MaterialTheme.colorScheme.outline,
                    nextMovePosition = nextMovePosition,
                )
            } else {
                this
            }
        },
    ) {
        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(boardCornerRadius))
                .background(background)
                .drawGaps(color = gaps),
        ) {
            for (rowIndex in 0 until BoardHeight) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (colIndex in 0 until BoardWidth) {
                        val disk = board.getAt(colIndex, rowIndex)
                        val overlayItem = overlayFactory(colIndex, rowIndex)

                        key(colIndex, rowIndex, disk != null, overlayItem?.composeKey, ended) {
                            BoardCell(
                                disk = disk,
                                overlayItem = overlayItem,
                                colIndex = colIndex,
                                rowIndex = rowIndex,
                                ended = ended,
                                isClickable = isClickable,
                                onCellClick = onCellClick,
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Modifier.drawGaps(color: Color) = this then Modifier
    .drawWithCache {
        onDrawWithContent {
            val stepX = size.width / BoardWidth
            val stepY = size.height / BoardHeight
            val strokeWidth = 2F

            drawContent()
            for (n in 1.until(BoardWidth)) {
                drawLine(
                    color = color,
                    start = Offset(n * stepX, 0F),
                    end = Offset(n * stepX, size.height),
                    strokeWidth = strokeWidth,
                )
                drawLine(
                    color = color,
                    start = Offset(0F, n * stepY),
                    end = Offset(size.width, n * stepY),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }

private fun Modifier.drawPositions(
    textMeasurer: TextMeasurer,
    color: Color,
    nextMovePosition: Position?,
) = this then Modifier
    .padding(start = 24.dp, top = 24.dp)
    .drawWithCache {
        onDrawWithContent {
            val stepX = size.width / BoardWidth
            val stepY = size.height / BoardHeight
            val startX = 48F
            val startY = 32F
            val offsetX = -56F
            val offsetY = -68F
            val style = TextStyle(color = color)

            drawContent()
            for (x in 0.until(BoardWidth)) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = "${PositionLetters[x]}",
                    topLeft = Offset(startX + x * stepX, offsetY),
                    size = Size(stepX, 48F),
                    style = if (x == nextMovePosition?.x) style.copy(fontWeight = FontWeight.Bold) else style,
                )
            }
            for (y in 0.until(BoardHeight)) {
                drawText(
                    textMeasurer = textMeasurer,
                    text = "${PositionNumbers[y]}",
                    topLeft = Offset(offsetX, startY + y * stepY),
                    size = Size(48F, stepY),
                    style = if (y == nextMovePosition?.y) style.copy(fontWeight = FontWeight.Bold) else style,
                )
            }
        }
    }

@Composable
private fun RowScope.BoardCell(
    disk: Disk?,
    overlayItem: Sprite?,
    colIndex: Int,
    rowIndex: Int,
    ended: GameEnd?,
    isClickable: Boolean,
    onCellClick: (position: Position) -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(CELL_WEIGHT)
            .aspectRatio(1F)
            .clickable(enabled = isClickable) {
                onCellClick(colIndex to rowIndex)
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
