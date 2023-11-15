package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.data.ExcludeFromCoverage
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
) : IGameSettings {
    companion object {
        @ExcludeFromCoverage
        fun from(other: IGameSettings) = with(other) {
            GameSettings(
                displayOptions = displayOptions,
                lightStrategy = lightStrategy,
                darkStrategy = darkStrategy,
            )
        }
    }
}
