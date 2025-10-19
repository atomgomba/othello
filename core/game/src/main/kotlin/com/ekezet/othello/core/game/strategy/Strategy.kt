package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.game.strategy.internal.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.internal.PreferSidesDecoratorStrategy
import com.ekezet.othello.core.game.strategy.internal.RandomStrategy
import com.ekezet.othello.core.game.strategy.internal.SchizophrenicStrategy

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

fun Strategy.preferSides(): DecoratedStrategy =
    this as? PreferSidesDecoratorStrategy ?: PreferSidesDecoratorStrategy(this)

val Strategy?.isPreferSides: Boolean
    get() = this is PreferSidesDecoratorStrategy

val Strategy?.requiredName: String
    inline get() = this?.name ?: "Human"

val Strategy?.wrappedName: String
    get() = if (this is DecoratedStrategy) {
        wrapped.wrappedName
    } else {
        requiredName
    }

val Strategies: Set<Strategy?>
    get() = buildSet {
        add(HumanPlayer)
        add(NaiveMaxStrategy)
        add(RandomStrategy)
        add(SchizophrenicStrategy)
    }

val HumanPlayer: Strategy? = null
