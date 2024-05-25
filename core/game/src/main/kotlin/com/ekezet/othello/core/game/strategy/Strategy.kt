package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.OthelloGameState

interface Strategy {
    val name: String

    fun deriveNext(state: OthelloGameState): Position?

    companion object Factory
}

fun Strategy.Factory.ofName(name: String): Strategy? =
    Strategies.firstOrNull { it?.name == name }

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

val HumanPlayer: Strategy? = null
