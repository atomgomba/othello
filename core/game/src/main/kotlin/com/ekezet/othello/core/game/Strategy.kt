package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Position

fun interface Strategy {
    fun deriveNext(state: GameState): Position?
}
