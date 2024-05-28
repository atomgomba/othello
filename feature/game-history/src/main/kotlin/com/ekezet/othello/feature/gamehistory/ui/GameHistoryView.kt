package com.ekezet.othello.feature.gamehistory.ui

import androidx.compose.animation.core.Animatable
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.compose.LoopWrapper
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
            Modifier.fillMaxSize()
        ),
        floatingActionButton = {
            if (historyItems.isNotEmpty()) {
                ListNavigationFab(listState, historyItems.size - 1)
            }
        },
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
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

@Composable
private fun ListNavigationFab(
    listState: LazyListState,
    lastItemIndex: Int,
) {
    var isScrollingUp by remember { mutableStateOf(false) }
    var lastFirstVisibleItemIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { firstItemIndex ->
            isScrollingUp = firstItemIndex < lastFirstVisibleItemIndex
            lastFirstVisibleItemIndex = firstItemIndex
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val rotationDeg = remember { Animatable(0F) }

    LaunchedEffect(isScrollingUp) {
        if (isScrollingUp) {
            rotationDeg.animateTo(180F)
        } else {
            rotationDeg.animateTo(0F)
        }
    }

    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                if (isScrollingUp) {
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
