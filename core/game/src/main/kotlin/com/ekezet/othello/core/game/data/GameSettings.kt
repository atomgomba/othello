package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy

interface GameSettings {
    val lightStrategy: Strategy?
    val darkStrategy: Strategy?
    val displayOptions: BoardDisplayOptions
}

val GameSettings.isLightHumanOpponent: Boolean
    inline get() = lightStrategy == HumanPlayer

val GameSettings.isDarkHumanOpponent: Boolean
    inline get() = darkStrategy == HumanPlayer
