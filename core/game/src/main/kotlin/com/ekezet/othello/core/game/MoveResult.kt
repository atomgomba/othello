package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.state.CurrentGameState

sealed interface MoveResult {
    val state: CurrentGameState
}

data class NextTurn(override val state: CurrentGameState) : MoveResult
data class PassTurn(override val state: CurrentGameState) : MoveResult
data class Win(override val state: CurrentGameState, val winner: Disk) : MoveResult
data class Tie(override val state: CurrentGameState) : MoveResult
