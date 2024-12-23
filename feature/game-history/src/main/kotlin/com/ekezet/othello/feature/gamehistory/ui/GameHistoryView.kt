package com.ekezet.othello.feature.gamehistory.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ekezet.hurok.AnyActionEmitter
import com.ekezet.hurok.compose.LoopView
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
    parentEmitter: AnyActionEmitter?,
    listState: LazyListState,
    onTurnClick: (turn: Int) -> Unit,
    onCurrentTurnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LoopView(
        builder = GameHistoryLoop,
        args = args,
        parentEmitter = checkNotNull(parentEmitter) {
            "parentEmitter need to be set to emit GameHistory actions from the parent"
        },
    ) {
        GameHistoryViewImpl(
            listState = listState,
            onTurnClick = onTurnClick,
            onCurrentTurnClick = onCurrentTurnClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun GameHistoryState.GameHistoryViewImpl(
    listState: LazyListState,
    onTurnClick: (turn: Int) -> Unit,
    onCurrentTurnClick: () -> Unit,
    modifier: Modifier,
) {
    val notAllItemsVisible by remember {
        derivedStateOf { with(listState) { canScrollForward || canScrollBackward } }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AnimatedVisibility(visible = notAllItemsVisible) {
                ListNavigationFab(listState, historyItems.lastIndex)
            }
        },
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier.consumeWindowInsets(paddingValues),
            contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp),
        ) {
            itemsIndexed(items = historyItems, key = { _, item -> item.composeKey }) { index, item ->
                HistoryItemView(
                    item = item,
                    modifier = Modifier.clickable { onTurnClick(index) },
                    isGrayscaleMode = isGrayscaleMode,
                )
            }

            if (gameEnd != null) {
                item(key = "end-result") {
                    GameEndItemView(
                        gameEnd = gameEnd,
                        modifier = Modifier.clickable(onClick = onCurrentTurnClick),
                    )
                }
            }
        }
    }

    LaunchedEffect(historyItems.size, alwaysScrollToBottom) {
        if (alwaysScrollToBottom) {
            listState.animateScrollToItem(historyItems.lastIndex)
        }
    }
}

@Composable
private fun ListNavigationFab(
    listState: LazyListState,
    lastItemIndex: Int,
) {
    val isScrollingUp by remember {
        derivedStateOf { with(listState) { (lastScrolledBackward && canScrollBackward) || !canScrollForward } }
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
            contentDescription = stringResource(
                id = if (isScrollingUp) {
                    R.string.game_history__list__scroll_to_top
                } else {
                    R.string.game_history__list__scroll_to_bottom
                },
            ),
            modifier = Modifier.rotate(rotationDeg.value),
        )
    }
}
