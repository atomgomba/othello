package com.ekezet.othello.core.game.state

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.DiskCount
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.MoveResult
import com.ekezet.othello.core.game.ValidMove
import com.ekezet.othello.core.game.throwable.InvalidMoveException

interface OthelloGameState {
    val currentBoard: Board
    val history: MoveHistory
    val turn: Int
    val currentDisk: Disk
    val validMoves: Set<ValidMove>
    val diskCount: DiskCount
    val lastState: CurrentGameState

    @Throws(InvalidMoveException::class)
    fun proceed(moveAt: Position): MoveResult

    companion object
}
