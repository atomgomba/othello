package com.ekezet.othello.feature.gamehistory

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.ImageBitmap
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameHistory
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.state.PastGameState
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.ImmutableList

internal data class GameHistoryModel(
    val moveHistory: MoveHistory = emptyList(),
    val gameEnd: GameEnd? = null,
    val historyImages: Map<String, ImageBitmap> = emptyMap(),
    val gameSettings: GameSettings = GameSettings.Default,
    val historySettings: HistorySettings = HistorySettings.Default,
)

@Immutable
internal data class GameHistoryState(
    val historyItems: ImmutableList<HistoryItem>,
    val gameEnd: GameEnd?,
    val lastState: PastGameState,
    val isGrayscaleMode: Boolean,
    val alwaysScrollToBottom: Boolean,
) : ViewState<GameHistoryModel, GameHistoryDependency>()

@Stable
data class GameHistoryArgs(
    val history: GameHistory,
    val historyImages: Map<String, ImageBitmap>,
    val gameSettings: GameSettings,
    val historySettings: HistorySettings,
)

internal object GameHistoryDependency
