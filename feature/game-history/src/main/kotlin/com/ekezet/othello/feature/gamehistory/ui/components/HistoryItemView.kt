package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.components.GamePieceWithBorder
import com.ekezet.othello.core.ui.theme.BoardBackground
import com.ekezet.othello.core.ui.theme.BoardBackgroundGrayscale
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem

@Composable
internal fun HistoryItemView(
    item: HistoryItem,
    isGrayscaleMode: Boolean = false,
) = with(item) {
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
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceDim,
                    modifier = Modifier.size(24.dp),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "$turn",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.align(Center),
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    GamePieceWithBorder(
                        disk = disk,
                        modifier = Modifier.height(IntrinsicSize.Max),
                    )

                    Text(
                        text = if (move == null) {
                            stringResource(id = R.string.game_history__list__pass)
                        } else {
                            stringResource(id = R.string.game_history__list__move, move.asString())
                        },
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                Text(
                    text = "${item.darkCount} vs ${item.lightCount}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(start = 16.dp),
                )
            }

            Spacer(Modifier.weight(1F))

            if (image != null) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isGrayscaleMode) BoardBackgroundGrayscale else BoardBackground,
                ) {
                    Image(
                        bitmap = image,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
        )
    }
}
