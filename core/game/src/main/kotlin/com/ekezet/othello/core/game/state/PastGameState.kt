package com.ekezet.othello.core.game.state

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.MoveResult
import com.ekezet.othello.core.game.ValidMove
import com.ekezet.othello.core.game.data.StartBoard
import com.ekezet.othello.core.game.findValidMoves
import com.ekezet.othello.core.game.throwable.InvalidPastMoveException

@ConsistentCopyVisibility
data class PastGameState internal constructor(
    private val wrapped: CurrentGameState,
) : OthelloGameState by wrapped {
    override val currentDisk: Disk = !wrapped.currentDisk

    override val validMoves: Set<ValidMove> by lazy {
        // find valid moves for the previous board state
        (pastMoves.dropLast(1).lastOrNull()?.board ?: StartBoard).findValidMoves(currentDisk)
    }

    @Throws(InvalidPastMoveException::class)
    override fun proceed(moveAt: Position): MoveResult {
        throw InvalidPastMoveException()
    }
}
