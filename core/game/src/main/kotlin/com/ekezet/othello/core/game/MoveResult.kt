package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Disk

sealed interface MoveResult {
    val state: OthelloGameState
}

data class NextTurn(override val state: OthelloGameState) : MoveResult
data class PassTurn(override val state: OthelloGameState) : MoveResult
data class Win(override val state: OthelloGameState, val winner: Disk) : MoveResult
data class Tie(override val state: OthelloGameState) : MoveResult
