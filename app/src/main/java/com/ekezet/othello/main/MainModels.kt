package com.ekezet.othello.main

import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HasGameSettingsStore
import com.ekezet.othello.core.game.store.MoveHistoryStore
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardEmitter
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
    val hasGameHistory: Boolean = false,
) : IGameSettings

@Immutable
internal data class MainState(
    val hasGameHistory: Boolean,
) : ViewState<MainModel, MainDependency>()

internal class MainDependency(
    gameSettingsStore: GameSettingsStore? = null,
    moveHistoryStore: MoveHistoryStore? = null,
) : KoinComponent, HasGameSettingsStore {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
    val moveHistoryStore: MoveHistoryStore = moveHistoryStore ?: get()

    var gameBoardEmitter: GameBoardEmitter? = null
    var collectJob: Job? = null
}

internal typealias MainActionEmitter = ActionEmitter<MainModel, MainDependency>
