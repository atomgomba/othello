package com.ekezet.othello.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultDarkStrategy
import com.ekezet.othello.core.game.data.DefaultLightStrategy
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.dependency.GameSettingsPublisher
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardEmitter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val darkStrategy: Strategy? = DefaultDarkStrategy,
    override val lightStrategy: Strategy? = DefaultLightStrategy,
    override val displayOptions: BoardDisplayOptions = BoardDisplayOptions.Default,
    val hasGameHistory: Boolean = false,
) : IGameSettings

@Immutable
internal data class MainState(
    val hasGameHistory: Boolean,
) : ViewState<MainModel, MainDependency>()

@Stable
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
