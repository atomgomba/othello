package com.ekezet.othello.feature.gamehistory

import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.store.MoveHistoryStore
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class GameHistoryModel(
    val moveHistory: MoveHistory = emptyList(),
)

@Immutable
internal data object GameHistoryState : ViewState<GameHistoryModel, GameHistoryDependency>()

internal class GameHistoryDependency(
    moveHistoryStore: MoveHistoryStore? = null,
) : KoinComponent {
    val moveHistoryStore: MoveHistoryStore = moveHistoryStore ?: get()
}

internal typealias GameHistoryEmitter = ActionEmitter<GameHistoryModel, GameHistoryDependency>
