package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.DiskCount
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.deepClone
import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.data.models.putAt
import com.ekezet.othello.core.data.models.putAtAndClone
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.data.defaultGameState
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import timber.log.Timber

data class OthelloGameState(
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

    val diskCount: DiskCount by lazy {
        currentBoard.diskCount
    }

    val lastState: OthelloGameState
        get() = history.last().run {
            OthelloGameState(currentBoard = board, history = history.dropLast(1))
        }

    @Throws(InvalidMoveException::class)
    fun proceed(moveAt: Position?): MoveResult {
        if (moveAt == null || validMoves.isInvalid(moveAt)) {
            Timber.w("Invalid move attempt: $currentDisk @ ${moveAt?.asString()}")
            throw InvalidMoveException()
        }
        Timber.d("Move (turn: ${turn + 1}): $currentDisk @ ${moveAt.asString()}")
        val nextBoard = currentBoard.putAtAndClone(moveAt, currentDisk)
        val validSegments = validMoves
            .filter { it.position == moveAt }
            .map { it.segment }
        for (segment in validSegments) {
            for (position in segment.parts()) {
                nextBoard.putAt(position, currentDisk)
            }
        }
        val nextState = copy(
            currentBoard = nextBoard,
            history = history + PastMove(
                board = nextBoard.deepClone(),
                moveAt = moveAt,
                disk = currentDisk,
                turn = turn + 1,
            ),
        )
        Timber.d("Next board:\n" + BoardSerializer.toString(nextBoard))
        return generateNextTurn(nextState, nextBoard)
    }

    private fun generateNextTurn(
        nextState: OthelloGameState,
        nextBoard: Board,
    ): MoveResult = if (nextState.validMoves.isNotEmpty()) {
        // next player has a valid move, continue the game
        Timber.d("Continue turn ${nextState.turn + 1}")
        NextTurn(state = nextState)
    } else {
        val nextDisk = nextState.currentDisk
        val hasMoreValidMoves = nextBoard
            .findValidMoves(currentDisk)
            .isNotEmpty()
        if (hasMoreValidMoves) {
            // current player still has a valid move, next player passes
            Timber.d("Player passed ($nextDisk)")
            PassTurn(
                state = nextState.copy(
                    history = nextState.history + PastMove(
                        board = nextBoard.deepClone(),
                        moveAt = null,
                        disk = nextDisk,
                        turn = turn + 1,
                    ),
                ),
            )
        } else {
            // nobody can move, it's a win or a tie
            val (numDark, numLight) = nextState.diskCount
            if (numDark == numLight) {
                Timber.d("It's a tie!")
                Tie(state = nextState)
            } else {
                val winner = if (numDark < numLight) Disk.Light else Disk.Dark
                Timber.d("We have a winner! ($winner)")
                Win(state = nextState, winner = winner)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OthelloGameState

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
        fun new(board: Board? = null) =
            if (board == null) {
                defaultGameState
            } else {
                OthelloGameState(
                    currentBoard = board,
                    history = emptyList(),
                )
            }
    }
}
