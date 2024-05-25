package com.ekezet.othello.feature.gamehistory

import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.feature.gamehistory.ui.viewModels.HistoryItem
import kotlinx.collections.immutable.ImmutableList

internal data class GameHistoryModel(
    val moveHistory: MoveHistory = emptyList(),
)

@Immutable
internal data class GameHistoryState(
    val historyItems: ImmutableList<HistoryItem>,
) : ViewState<GameHistoryModel, GameHistoryDependency>()

internal object GameHistoryDependency

internal typealias GameHistoryActionEmitter = ActionEmitter<GameHistoryModel, GameHistoryDependency>
