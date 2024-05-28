package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.components.GamePiece
import com.ekezet.othello.core.ui.components.GamePieceWithBorder
import com.ekezet.othello.core.ui.theme.BoardBackground
import com.ekezet.othello.core.ui.viewModels.BoardList
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem

@Composable
internal fun HistoryItemView(item: HistoryItem) = with(item) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "$turn",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.width(32.dp),
                )

                Row(
                    modifier = Modifier.height(IntrinsicSize.Max),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    GamePieceWithBorder(
                        disk = disk,
                        modifier = Modifier.size(48.dp),
                    )

                    Text(
                        text = if (move == null) {
                            stringResource(id = R.string.game_history__list__pass)
                        } else {
                            stringResource(id = R.string.game_history__list__move, move.asString())
                        },
                        style = MaterialTheme.typography.headlineMedium,
                    )
                }
            }

            Spacer(Modifier.weight(1F))

            key(turn) {
                GameBoardThumbnail(board = board)
            }
        }

        HorizontalDivider()
    }
}

@Composable
private fun GameBoardThumbnail(board: BoardList) {
    Surface(
        color = BoardBackground,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(4.dp),
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            for (row in board) {
                Row {
                    for (disk in row) {
                        Box(
                            modifier = Modifier.size(12.dp),
                        ) {
                            disk?.let { disk ->
                                GamePiece(
                                    disk = disk,
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
