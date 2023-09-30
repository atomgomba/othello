package com.ekezet.othello.feature.gameboard.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.feature.gameboard.GameEnd
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import com.ekezet.othello.feature.gameboard.ui.viewModels.getAt

private val borderWidth = 1.dp
private const val LOSER_ALPHA = .333F
private const val CELL_WEIGHT = 1F / BoardWidth

@Composable
internal fun GameBoard(
    board: BoardList,
    modifier: Modifier = Modifier,
    background: Color = Color(0xFF338033),
    showPositions: Boolean = false,
    ended: GameEnd? = null,
    overlay: BoardOverlayList? = null,
    onCellClick: (x: Int, y: Int) -> Unit = { _, _ -> },
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = modifier.then(
                Modifier
                    .wrapContentSize()
                    .background(color = Color.White.copy(alpha = .75F))
                    .padding(
                        bottom = borderWidth,
                        end = borderWidth,
                    ),
            ),
        ) {
            for (rowIndex in 0 until BoardHeight) {
                Row(
                    modifier = Modifier.wrapContentSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    for (colIndex in 0 until BoardWidth) {
                        val disk = board.getAt(colIndex, rowIndex)
                        val overlayItem = overlay?.getAt(colIndex, rowIndex)
                        key(colIndex, rowIndex, disk, overlayItem?.composeKey) {
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = borderWidth,
                                        start = borderWidth,
                                    )
                                    .background(color = background)
                                    .weight(CELL_WEIGHT)
                                    .aspectRatio(1F)
                                    .clickable {
                                        onCellClick(
                                            colIndex,
                                            rowIndex,
                                        )
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
                                        modifier = Modifier.alpha(
                                            alpha = alpha,
                                        ),
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
}
