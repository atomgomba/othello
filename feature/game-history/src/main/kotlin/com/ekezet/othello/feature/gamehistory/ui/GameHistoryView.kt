package com.ekezet.othello.feature.gamehistory.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.feature.gamehistory.GameHistoryArgs
import com.ekezet.othello.feature.gamehistory.GameHistoryLoop
import com.ekezet.othello.feature.gamehistory.GameHistoryState
import com.ekezet.othello.feature.gamehistory.ui.components.GameEndItemView
import com.ekezet.othello.feature.gamehistory.ui.components.HistoryItemView
import kotlinx.coroutines.launch

@Composable
fun GameHistoryView(
    args: GameHistoryArgs,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LoopWrapper(
        builder = GameHistoryLoop,
        args = args,
    ) {
        GameHistoryViewImpl(
            listState = listState,
            modifier = modifier,
        )
    }
}

@Composable
private fun GameHistoryState.GameHistoryViewImpl(
    listState: LazyListState,
    modifier: Modifier,
) {
    Scaffold(
        modifier = modifier.then(
            Modifier
                .padding(16.dp)
                .fillMaxSize()
        ),
        floatingActionButton = {
            if (historyItems.isNotEmpty()) {
                ListNavigationFab(listState, historyItems.size - 1)
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.game_history__title__num_of_moves, historyItems.size),
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

                if (gameEnd != null) {
                    item(key = "end-result") {
                        GameEndItemView(gameEnd, lastState)
                    }
                }

                item("bottom-spacer") {
                    Spacer(Modifier.height(96.dp))
                }
            }
        }
    }
}

@Composable
private fun ListNavigationFab(
    listState: LazyListState,
    lastItemIndex: Int,
) {
    var canScrollUp by remember { mutableStateOf(false) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { firstItemIndex ->
            canScrollUp = lastItemIndex / 2 < firstItemIndex
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val rotationDeg = remember { Animatable(0F) }

    LaunchedEffect(canScrollUp) {
        if (canScrollUp) {
            rotationDeg.animateTo(180F)
        } else {
            rotationDeg.animateTo(0F)
        }
    }

    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                if (canScrollUp) {
                    listState.animateScrollToItem(0)
                } else {
                    listState.animateScrollToItem(lastItemIndex)
                }
            }
        },
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = null,
            modifier = Modifier.rotate(rotationDeg.value),
        )
    }
}
