package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState

interface Strategy {
    val name: String

    fun deriveNext(state: GameState): Position?
}

val Strategy?.requiredName: String
    get() = this?.name ?: "Human"

val Strategy?.wrappedName: String
    get() = if (this is DecoratedStrategy) {
        wrapped.wrappedName
    } else {
        requiredName
    }

val Strategies
    get() = buildSet {
        add(HumanPlayer)
        add(NaiveMaxStrategy)
        add(RandomStrategy)
    }
