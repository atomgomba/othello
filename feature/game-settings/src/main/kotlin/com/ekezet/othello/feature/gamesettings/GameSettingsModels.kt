package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.strategy.Strategy

data class GameSettingsModel(
    val isLightPreferSides: Boolean = false,
    val isDarkPreferSides: Boolean = false,
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
) : IGameSettings

data object GameSettingsDependency

internal data object GameSettingsState

typealias GameSettingsScope = LoopScope<GameSettingsModel, GameSettingsDependency>
