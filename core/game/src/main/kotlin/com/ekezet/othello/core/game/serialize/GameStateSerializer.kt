package com.ekezet.othello.core.game.serialize

import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.BoardFactory
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.strategy.requiredName

object GameStateSerializer {
    fun toString(gameState: GameState, gameSetting: GameSettings) = buildString {
        appendLine(serializedHeader(gameSetting))
        for ((i, pastMove) in gameState.history.withIndex()) {
            appendLine(pastMoveToString(i, pastMove))
        }
        val lastMove = gameState.history.lastOrNull() ?: return@buildString
        val (numDark, numLight) = lastMove.board.diskCount
        appendLine("Disks: Dark: $numDark; Light: $numLight")
    }

    private fun pastMoveToString(turn0: Int, pastMove: PastMove) = buildString {
        with(pastMove) {
            if (moveAt == null) {
                appendLine("## Turn ${turn0 + 1}: $disk Passes")
            } else {
                appendLine("## Turn ${turn0 + 1}: $disk @ ${moveAt.asString()}")
            }
        }
        appendLine(BoardSerializer.toString(pastMove.board))
    }

    fun fromString(data: String): GameState {
        TODO()
    }

    private fun serializedHeader(gameSetting: GameSettings) = buildString {
        appendLine(
            "# Othello (${gameSetting.darkStrategy.requiredName} vs. ${gameSetting.lightStrategy.requiredName})"
        )
        appendLine(BoardSerializer.toString(BoardFactory.starter()))
    }
}
