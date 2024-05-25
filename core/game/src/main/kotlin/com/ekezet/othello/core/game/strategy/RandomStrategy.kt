package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.OthelloGameState

/**
 * Just picks a random valid move.
 */
data object RandomStrategy : Strategy {
    override val name: String
        get() = "Random Pick"

    override fun deriveNext(state: OthelloGameState): Position? =
        state.validMoves.shuffled().firstOrNull()?.position
}
