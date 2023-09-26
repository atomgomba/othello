package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.Strategy

/**
 * Just picks a random valid move.
 */
class RandomStrategy : Strategy {
    override val name: String
        get() = "Random Ron"

    override fun deriveNext(state: GameState): Position? =
        state.validMoves.shuffled().firstOrNull()?.position
}
