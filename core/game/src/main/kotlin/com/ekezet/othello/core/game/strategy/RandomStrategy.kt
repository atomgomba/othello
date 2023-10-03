package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState

/**
 * Just picks a random valid move.
 */
object RandomStrategy : Strategy {
    override val name: String
        get() = "Random Pick"

    override fun deriveNext(state: GameState): Position? =
        state.validMoves.shuffled().firstOrNull()?.position
}
