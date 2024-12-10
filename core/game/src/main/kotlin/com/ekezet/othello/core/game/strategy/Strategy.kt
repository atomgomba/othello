package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.OthelloGameState

interface Strategy {
    val name: String

    /**
     * Returns the next valid move derived from the [state], or null if there's none.
     */
    fun deriveNext(state: OthelloGameState): Position?

    companion object Factory
}

fun Strategy.Factory.ofName(name: String): Strategy? =
    Strategies.firstOrNull { it?.name == name }

val Strategy?.requiredName: String
    inline get() = this?.name ?: "Human"

val Strategy?.wrappedName: String
    get() = if (this is DecoratedStrategy) {
        wrapped.wrappedName
    } else {
        requiredName
    }

val Strategies
    inline get() = buildSet {
        add(HumanPlayer)
        add(NaiveMaxStrategy)
        add(RandomStrategy)
        add(SchizophrenicStrategy)
    }

val HumanPlayer: Strategy? = null
