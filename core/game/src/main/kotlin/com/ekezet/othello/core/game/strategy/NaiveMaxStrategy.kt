package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.parts
import com.ekezet.othello.core.game.state.OthelloGameState

/**
 * Simply takes the move which results in the highest number of disks flipped.
 */
data object NaiveMaxStrategy : Strategy {
    override val name: String = "Max Flip"

    override fun deriveNext(state: OthelloGameState): Position? =
        state.validMoves.maxByOrNull { it.segment.parts().size }?.position
}
