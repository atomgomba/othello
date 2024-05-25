package com.ekezet.othello.feature.gamehistory.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ekezet.hurok.compose.LoopWrapper
import com.ekezet.othello.feature.gamehistory.GameHistoryLoop
import com.ekezet.othello.feature.gamehistory.GameHistoryState

@Composable
fun GameHistoryView(
    modifier: Modifier = Modifier,
) {
    LoopWrapper(builder = GameHistoryLoop) {
        GameHistoryViewImpl(
            modifier = modifier,
        )
    }
}

@Composable
private fun GameHistoryState.GameHistoryViewImpl(
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        Text("History")
    }
}
