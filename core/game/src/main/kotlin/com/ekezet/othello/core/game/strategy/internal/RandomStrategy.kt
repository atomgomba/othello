package com.ekezet.othello.core.game.strategy.internal

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.game.strategy.Strategy

/**
 * Just picks a random valid move.
 */
internal data object RandomStrategy : Strategy {
    override val name: String = "Random Pick"

    override fun deriveNext(state: OthelloGameState): Position? =
        state.validMoves.shuffled().firstOrNull()?.position
}
