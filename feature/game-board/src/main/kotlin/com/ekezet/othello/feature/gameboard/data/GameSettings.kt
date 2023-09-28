package com.ekezet.othello.feature.gameboard.data

import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.DisplayOptions

interface GameSettings {
    val opponentStrategy: Strategy?
    val displayOptions: DisplayOptions
}

val GameSettings.isHumanOpponent: Boolean
    inline get() = opponentStrategy == HumanPlayer
