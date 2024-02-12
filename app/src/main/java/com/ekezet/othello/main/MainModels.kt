package com.ekezet.othello.main

import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HasGameSettingsStore
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardEmitter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
) : IGameSettings

@Immutable
internal data object MainState : ViewState<MainModel, MainDependency>()

internal class MainDependency(
    gameSettingsStore: GameSettingsStore? = null,
) : KoinComponent, HasGameSettingsStore {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()

    var gameBoardEmitter: GameBoardEmitter? = null
}
