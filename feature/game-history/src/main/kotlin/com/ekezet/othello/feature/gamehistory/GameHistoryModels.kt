package com.ekezet.othello.feature.gamehistory

import android.graphics.Bitmap
import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameHistory
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.state.PastGameState
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.ImmutableList

internal data class GameHistoryModel(
    val moveHistory: MoveHistory = emptyList(),
    val gameEnd: GameEnd? = null,
    val historyImages: Map<String, Bitmap> = emptyMap(),
)

@Immutable
internal data class GameHistoryState(
    val historyItems: ImmutableList<HistoryItem>,
    val gameEnd: GameEnd?,
    val lastState: PastGameState,
) : ViewState<GameHistoryModel, GameHistoryDependency>()

data class GameHistoryArgs(
    val history: GameHistory,
    val historyImages: Map<String, Bitmap>,
)

internal object GameHistoryDependency

internal typealias GameHistoryActionEmitter = ActionEmitter<GameHistoryModel, GameHistoryDependency>
