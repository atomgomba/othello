package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.data.models.putAndCloneAt
import com.ekezet.othello.core.data.models.putAt
import com.ekezet.othello.core.data.serialize.serialize
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import timber.log.Timber

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

    /**
     * dark, light
     */
    val diskCount: DiskCount by lazy {
        currentBoard
            .flatten()
            .filterNotNull()
            .fold(DiskCount(0, 0)) { acc, disk ->
                DiskCount(
                    first = if (disk.isDark) acc.first + 1 else acc.first,
                    second = if (disk.isLight) acc.second + 1 else acc.second,
                )
            }
    }

    @Throws(
        InvalidMoveException::class,
    )
    fun proceed(nextMove: NextMove): MoveResult {
        val (pos, disk) = nextMove
        if (validMoves.isInvalid(pos)) {
            Timber.w("Invalid move: $disk @ ${pos.serialize()}")
            throw InvalidMoveException()
        }
        Timber.d("Next move (turn: ${turn + 1}): $disk @ ${pos.serialize()}")
        val nextBoard = currentBoard.putAndCloneAt(pos, disk)
        val validSegments = validMoves
            .filter { it.position == pos }
            .map { it.segment }
        for (segment in validSegments) {
            val parts = segment.parts()
            for (position in parts) {
                nextBoard.putAt(position, currentDisk)
            }
        }
        val nextState = copy(
            currentBoard = nextBoard,
            history = history + PastMove(board = currentBoard, move = nextMove),
        )
        return if (nextState.validMoves.isNotEmpty()) {
            // next player has a valid move, continue the game
            Timber.d("Continue turn ${nextState.turn + 1}")
            NextTurn(state = nextState)
        } else {
            val hasMoreValidMoves = nextBoard
                .findValidMoves(disk)
                .isNotEmpty()
            if (hasMoreValidMoves) {
                // current player still has a valid move, next player passes
                Timber.d("Player passed ($disk)")
                PassTurn(
                    state = nextState.copy(
                        history = nextState.history + nextState.history,
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
    }

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
        fun new(board: Board = BoardFactory.starter()) = GameState(
            currentBoard = board,
            history = emptyList(),
        )
    }
}

typealias DiskCount = Pair<Int, Int>

val DiskCount.numDark: Int
    inline get() = first

val DiskCount.numLight: Int
    inline get() = second
