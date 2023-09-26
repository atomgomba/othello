package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.proceedAt
import com.ekezet.othello.core.data.models.putAt
import com.ekezet.othello.core.game.error.InvalidMoveException
import java.util.Stack

data class GameState(
    val currentBoard: Board,
    val history: MoveHistory,
) {
    val turn: Int get() = history.size

    val currentDisk: Disk
        get() = if ((turn % 2) == 0) {
            Disk.Dark
        } else {
            Disk.Light
        }

    val validMoves: Set<ValidMove> by lazy {
        currentBoard.findValidMoves(currentDisk)
    }

    @Throws(InvalidMoveException::class)
    fun proceed(nextMove: NextMove): GameState {
        val (pos, disk) = nextMove
        if (validMoves.isInvalid(pos)) {
            throw InvalidMoveException()
        }
        val nextBoard = currentBoard.proceedAt(pos, disk)
        val segments = validMoves.filter { it.position == pos }.map { it.segment }
        for (segment in segments) {
            val parts = segment.parts()
            for (position in parts) {
                nextBoard.putAt(position, currentDisk)
            }
        }
        return copy(
            currentBoard = nextBoard,
            history = history.apply {
                add(PastMove(board = currentBoard, move = nextMove))
            },
        )
    }

    fun passTurn() =
        copy(
            currentBoard = currentBoard,
            history = history.apply {
                add(PastMove(board = currentBoard, move = null))
            },
        )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (!currentBoard.contentDeepEquals(other.currentBoard)) return false
        if (history != other.history) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentBoard.contentDeepHashCode()
        result = 31 * result + history.hashCode()
        return result
    }

    companion object {
        fun new(board: Board) = GameState(
            currentBoard = board,
            history = Stack(),
        )
    }
}
