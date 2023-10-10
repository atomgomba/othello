package com.ekezet.othello.feature.gameboard.ui.components

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
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.serialize.PositionLetters
import com.ekezet.othello.core.ui.components.GamePiece
import com.ekezet.othello.feature.gameboard.GameEnd
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.getAt

internal val borderWidth = 1.dp
private const val LOSER_ALPHA = .333F
private const val CELL_WEIGHT = 1F / BoardWidth
private val positionSize = 24.dp
private val boardCornerRadius = 16.dp

@ExperimentalLayoutApi
@Composable
internal fun GameBoard(
    board: BoardList,
    modifier: Modifier = Modifier,
    background: Color = Color(0xFF338033),
    showPositions: Boolean = false,
    ended: GameEnd? = null,
    overlay: BoardOverlayList? = null,
    isClickable: Boolean = true,
    onCellClick: (x: Int, y: Int) -> Unit = { _, _ -> },
) {
    Column(modifier = modifier) {
        Row {
            GameBoardImpl(
                board = board,
                background = background,
                showPositions = showPositions,
                ended = ended,
                overlay = overlay,
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
    background: Color = Color(0xFF338033),
    showPositions: Boolean,
    ended: GameEnd? = null,
    overlay: BoardOverlayList? = null,
    isClickable: Boolean = true,
    onCellClick: (x: Int, y: Int) -> Unit = { _, _ -> },
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(borderWidth),
    ) {
        for (rowIndex in 0 until BoardHeight) {
            if (showPositions && rowIndex == 0) {
                HorizontalPositions()
            }

            Row(
                modifier = Modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(borderWidth),
            ) {
                for (colIndex in 0 until BoardWidth) {
                    val disk = board.getAt(colIndex, rowIndex)
                    val overlayItem = overlay?.getAt(colIndex, rowIndex)

                    if (showPositions && colIndex == 0) {
                        VerticalPosition(rowIndex + 1)
                    }

                    key(colIndex, rowIndex, disk, overlayItem?.composeKey, ended) {
                        Box(
                            modifier = Modifier
                                .roundedBoard(colIndex, rowIndex)
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
private fun HorizontalPositions() {
    Row(
        modifier = Modifier
            .padding(start = positionSize)
            .fillMaxWidth()
            .height(positionSize)
    ) {
        for (letter in PositionLetters) {
            Text(
                text = letter.toString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier.weight(CELL_WEIGHT)
            )
        }
    }
}

@ExperimentalLayoutApi
@Composable
private fun VerticalPosition(number: Int) {
    Column {
        Box {
            Text(
                text = number.toString(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .width(positionSize)
                    .align(Alignment.Center),
            )
        }
    }
}

private fun Modifier.roundedBoard(x: Int, y: Int) =
    if (x == 0 && y == 0) {
        clip(shape = RoundedCornerShape(topStart = boardCornerRadius))
    } else if (x == 7 && y == 0) {
        clip(shape = RoundedCornerShape(topEnd = boardCornerRadius))
    } else if (x == 0 && y == 7) {
        clip(shape = RoundedCornerShape(bottomStart = boardCornerRadius))
    } else if (x == 7 && y == 7) {
        clip(shape = RoundedCornerShape(bottomEnd = boardCornerRadius))
    } else {
        this
    }
