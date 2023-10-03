package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy

interface IGameSettings {
    val displayOptions: BoardDisplayOptions
    val lightStrategy: Strategy?
    val darkStrategy: Strategy?
}

data class GameSettings(
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val darkStrategy: Strategy? = defaultDarkStrategy,
) : IGameSettings

val GameSettings.isLightHumanOpponent: Boolean
    inline get() = lightStrategy == HumanPlayer

val GameSettings.isDarkHumanOpponent: Boolean
    inline get() = darkStrategy == HumanPlayer
