package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.state.PastGameState

data class PastMove(
    val board: Board,
    val moveAt: Position?,
    val turn: Int,
    val disk: Disk,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PastMove

        if (!board.contentDeepEquals(other.board)) return false
        if (moveAt != other) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + (moveAt?.hashCode() ?: 0)
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
