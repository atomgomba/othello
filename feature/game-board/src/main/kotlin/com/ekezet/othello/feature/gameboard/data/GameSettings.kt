package com.ekezet.othello.feature.gameboard.data

import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.DisplayOptions

interface GameSettings {
    val gameState: GameState
    val opponentStrategy: Strategy?
    val displayOptions: DisplayOptions
}
