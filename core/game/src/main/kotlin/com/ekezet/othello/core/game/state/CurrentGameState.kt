package com.ekezet.othello.core.game.state

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
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.MoveResult
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.ValidMove
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.findValidMoves
import com.ekezet.othello.core.game.isInvalid
import com.ekezet.othello.core.game.parts
import com.ekezet.othello.core.game.throwable.InvalidNewMoveException
import timber.log.Timber

@ConsistentCopyVisibility
data class CurrentGameState internal constructor(
    override val board: Board,
    override val pastMoves: MoveHistory,
) : OthelloGameState {
    override val turn: Int = pastMoves.size

    override val currentDisk: Disk = if ((turn % 2) == 0) {
        Disk.Dark
    } else {
        Disk.Light
    }

    override val validMoves: Set<ValidMove> by lazy {
        board.findValidMoves(currentDisk)
    }

    override val diskCount: DiskCount by lazy {
        board.diskCount
    }

    override val lastState: CurrentGameState by lazy {
        pastMoves.last().run {
            copy(pastMoves = pastMoves.dropLast(1))
        }
    }

    @Throws(InvalidNewMoveException::class)
    override fun proceed(moveAt: Position): MoveResult {
        if (validMoves.isInvalid(moveAt)) {
            throw InvalidNewMoveException(disk = currentDisk, invalidPosition = moveAt)
        }
        Timber.i("Move (turn: ${turn + 1}): $currentDisk @ ${moveAt.asString()}")
        val nextBoard = board.putAtAndClone(moveAt, currentDisk)
        val validSegments = validMoves
            .filter { it.position == moveAt }
            .map { it.segment }
        for (segment in validSegments) {
            for (position in segment.parts()) {
                nextBoard.putAt(position, currentDisk)
            }
        }
        val nextState = copy(
            board = nextBoard,
            pastMoves = pastMoves + PastMove(
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
        nextState: CurrentGameState,
        nextBoard: Board,
    ): MoveResult = if (nextState.validMoves.isNotEmpty()) {
        // next player has a valid move, continue the game
        Timber.i("Continue turn ${nextState.turn + 1}")
        NextTurn(state = nextState)
    } else {
        val hasMoreValidMoves = nextBoard
            .findValidMoves(currentDisk)
            .isNotEmpty()
        if (hasMoreValidMoves) {
            // current player still has a valid move, next player passes
            val nextDisk = nextState.currentDisk
            Timber.i("Player passed ($nextDisk)")
            PassTurn(
                state = nextState.copy(
                    pastMoves = nextState.pastMoves + PastMove(
                        board = nextBoard.deepClone(),
                        moveAt = null,
                        disk = nextDisk,
                        turn = nextState.turn + 1,
                    ),
                ),
            )
        } else {
            // nobody can move, it's a win or a tie
            val (numDark, numLight) = nextState.diskCount
            if (numDark == numLight) {
                Timber.i("It's a tie!")
                Tie(state = nextState)
            } else {
                val winner = if (numDark < numLight) Disk.Light else Disk.Dark
                Timber.i("We have a winner! ($winner)")
                Win(state = nextState, winner = winner)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CurrentGameState

        if (!board.contentDeepEquals(other.board)) return false
        if (pastMoves != other.pastMoves) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.contentDeepHashCode()
        result = 31 * result + pastMoves.hashCode()
        return result
    }

    companion object
}

fun CurrentGameState.Companion.new(board: Board): OthelloGameState =
    CurrentGameState(
        board = board,
        pastMoves = emptyList(),
    )
