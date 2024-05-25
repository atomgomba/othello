package com.ekezet.othello.feature.gamehistory.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.components.GamePieceWithBorder
import com.ekezet.othello.feature.gamehistory.GameHistoryLoop
import com.ekezet.othello.feature.gamehistory.GameHistoryState
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem

@Composable
fun GameHistoryView(
    args: MoveHistory,
    modifier: Modifier = Modifier,
) {
    LoopWrapper(
        builder = GameHistoryLoop,
        args = args,
    ) {
        GameHistoryViewImpl(
            modifier = modifier,
        )
    }
}

@Composable
private fun GameHistoryState.GameHistoryViewImpl(
    modifier: Modifier,
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.then(
            Modifier
                .padding(16.dp)
                .fillMaxSize()
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(
                id = R.string.game_history__title__num_of_moves,
                historyItems.size,
            ),
            style = MaterialTheme.typography.headlineSmall,
        )

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(items = historyItems, key = { it.composeKey }) {
                HistoryItemView(item = it)
            }
        }
    }
}

@Composable
private fun HistoryItemView(item: HistoryItem) = with(item) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "$turn", modifier = Modifier.width(32.dp))

        GamePieceWithBorder(
            disk = disk,
            modifier = Modifier.size(16.dp),
        )

        Text(
            text = move?.asString() ?: "passed"
        )
    }
}
