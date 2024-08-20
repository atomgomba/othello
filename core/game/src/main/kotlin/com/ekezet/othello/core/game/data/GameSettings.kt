package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.data.ExcludeFromCoverage
import com.ekezet.othello.core.game.strategy.Strategy

interface IGameSettings {
    val displayOptions: BoardDisplayOptions
    val lightStrategy: Strategy?
    val darkStrategy: Strategy?
    val confirmExit: Boolean

    fun containsDifferentStrategy(other: IGameSettings) =
        other.darkStrategy != darkStrategy || other.lightStrategy != lightStrategy
}

data class GameSettings(
    override val displayOptions: BoardDisplayOptions,
    override val lightStrategy: Strategy?,
    override val darkStrategy: Strategy?,
    override val confirmExit: Boolean,
) : IGameSettings {
    companion object
}

@ExcludeFromCoverage
infix fun GameSettings.Companion.from(other: IGameSettings) = with(other) {
    GameSettings(
        displayOptions = displayOptions,
        lightStrategy = lightStrategy,
        darkStrategy = darkStrategy,
        confirmExit = confirmExit,
    )
}
