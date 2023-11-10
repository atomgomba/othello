package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.core.game.parts

/**
 * Simply takes the move which results in the highest number of disks flipped.
 */
data object NaiveMaxStrategy : Strategy {
    override val name: String
        get() = "Max Flip"

    override fun deriveNext(state: OthelloGameState): Position? =
        state.validMoves.maxByOrNull { it.segment.parts().size }?.position
}
