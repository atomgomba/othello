package com.ekezet.othello.main

import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.dependency.GameSettingsPublisher
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardEmitter
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

@Immutable
internal data class MainArgs(
    val gameSettings: IGameSettings,
    val hasGameHistory: Boolean,
)

internal class MainDependency(
    gameSettingsStore: GameSettingsStore? = null,
    gameHistoryStore: GameHistoryStore? = null,
) : KoinComponent, GameSettingsPublisher {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
    override val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()

    var gameBoardEmitter: GameBoardEmitter? = null
}

internal typealias MainActionEmitter = ActionEmitter<MainModel, MainDependency>
