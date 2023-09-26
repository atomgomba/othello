package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Position

interface Strategy {
    val name: String

    fun deriveNext(state: GameState): Position?
}
