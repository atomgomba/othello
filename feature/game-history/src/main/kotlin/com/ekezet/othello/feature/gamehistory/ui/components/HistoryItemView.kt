package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.components.GameBoard
import com.ekezet.othello.core.ui.components.GamePieceWithBorder
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun HistoryItemView(item: HistoryItem) = with(item) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(IntrinsicSize.Min),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxHeight(),
            ) {
                Text(
                    text = "$turn",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .width(32.dp),
                )

                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(IntrinsicSize.Max),
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
                GameBoard(
                    board = board,
                    boardCornerRadius = 4.dp,
                    isClickable = false,
                    modifier = Modifier.size(96.dp),
                    overlayFactory = { colIndex, rowIndex ->
                        PastMoveIndicator.takeIf {
                            move != null && colIndex == move.x && rowIndex == move.y
                        }
                    },
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
        )
    }
}
