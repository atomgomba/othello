package com.ekezet.othello.core.game.state

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.MoveResult
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import com.ekezet.othello.core.game.throwable.InvalidPastMoveException

data class PastGameState(
    private val wrapped: CurrentGameState,
) : OthelloGameState by wrapped {
    @Throws(InvalidMoveException::class)
    override fun proceed(moveAt: Position): MoveResult {
        throw InvalidPastMoveException()
    }
}
