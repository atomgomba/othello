package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.state.PastGameState

data class PastMove(
    val board: Board,
    val moveAt: Position?,
    val turn: Int,
    val disk: Disk,
) {
    val renderId: String by lazy {
        moveAt.toString() + "-" + BoardSerializer.toString(board)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PastMove

        if (!board.contentDeepEquals(other.board)) return false
        if (moveAt != other.moveAt) return false
        if (turn != other.turn) return false
        if (disk != other.disk) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + (moveAt?.hashCode() ?: 0)
        result = 31 * result + turn
        result = 31 * result + disk.hashCode()
        return result
    }
}

typealias MoveHistory = List<PastMove>

fun MoveHistory.toPastGameState(fromIndex: Int = 0): PastGameState {
    val pastMoves = subList(fromIndex, size)
    val pastState = CurrentGameState(
        currentBoard = pastMoves.last().board,
        history = pastMoves,
    )
    return PastGameState(pastState)
}

sealed interface GameEnd {
    data class EndedWin(val winner: Disk) : GameEnd
    data object EndedTie : GameEnd
}

data class GameHistory(
    val history: MoveHistory,
    val gameEnd: GameEnd?,
)
