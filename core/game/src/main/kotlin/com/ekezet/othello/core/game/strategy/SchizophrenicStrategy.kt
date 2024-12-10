package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.OthelloGameState
import timber.log.Timber

/**
 * Picks a random strategy for the next move.
 */
data object SchizophrenicStrategy : Strategy {
    override val name: String = "Schizophrenic"

    private val strategies by lazy {
        Strategies.mapNotNull { strategy -> strategy?.takeUnless { it.name == name } }
    }

    override fun deriveNext(state: OthelloGameState): Position? {
        val strategy: Strategy = strategies.random()
        Timber.d("Chosen strategy (turn: ${state.turn}): $strategy")
        return strategy.deriveNext(state)
    }
}
