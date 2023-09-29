package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Disk

sealed interface MoveResult {
    val state: GameState
}

data class NextTurn(override val state: GameState) : MoveResult
data class PassTurn(override val state: GameState) : MoveResult
data class Win(override val state: GameState, val winner: Disk) : MoveResult
data class Tie(override val state: GameState) : MoveResult
