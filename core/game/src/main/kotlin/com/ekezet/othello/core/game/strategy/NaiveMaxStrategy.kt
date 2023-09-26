package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.parts

/**
 * Simply takes the move which results in the highest number of disks flipped.
 */
class NaiveMaxStrategy : Strategy {
    override val name: String
        get() = "Naive Max"

    override fun deriveNext(state: GameState): Position? =
        state.validMoves.maxByOrNull { it.segment.parts().size }?.position
}
