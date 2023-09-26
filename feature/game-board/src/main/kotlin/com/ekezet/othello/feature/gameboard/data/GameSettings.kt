package com.ekezet.othello.feature.gameboard.data

import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.DisplayOptions

interface GameSettings {
    val initialGameState: GameState
    val opponentStrategy: Strategy?
    val displayOptions: DisplayOptions
}
