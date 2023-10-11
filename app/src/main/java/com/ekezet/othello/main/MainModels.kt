package com.ekezet.othello.main

import android.content.Context
import androidx.compose.runtime.Stable
import com.ekezet.hurok.ParentLoop
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.dependency.HasGameSettingsStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
) : IGameSettings

@Stable
internal data class MainState(
    val onNewGameClick: () -> Unit,
    val onToggleIndicatorsClick: () -> Unit,
    val onShareGameClick: () -> Unit,
)

internal class MainDependency(
    androidContext: Context? = null,
    gameSettingsStore: GameSettingsStore? = null,
) : KoinComponent, HasGameSettingsStore {
    val androidContext: Context = androidContext ?: get()
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()

    var gameBoardScope: GameBoardScope? = null
        internal set
}

internal typealias MainLoopScope = ParentLoop<MainModel, MainDependency>