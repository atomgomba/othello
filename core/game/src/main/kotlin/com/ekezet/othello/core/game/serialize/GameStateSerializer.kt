package com.ekezet.othello.core.game.serialize

import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.BoardFactory
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.strategy.requiredName

object GameStateSerializer {
    fun toString(gameState: OthelloGameState, gameSetting: IGameSettings) = buildString {
        appendLine(serializedHeader(gameSetting))
        for (pastMove in gameState.history) {
            appendLine(pastMoveToString(pastMove))
        }
        val lastMove = gameState.history.lastOrNull() ?: return@buildString
        val (numDark, numLight) = lastMove.board.diskCount
        appendLine("Disks: Dark: $numDark; Light: $numLight")
    }

    private fun pastMoveToString(pastMove: PastMove) = buildString {
        with(pastMove) {
            if (moveAt == null) {
                appendLine("## Turn $turn: $disk Passes")
            } else {
                appendLine("## Turn $turn: $disk @ ${moveAt.asString()}")
            }
        }
        appendLine(BoardSerializer.toString(pastMove.board))
    }

    fun fromString(data: String): OthelloGameState {
        TODO()
    }

    private fun serializedHeader(gameSetting: IGameSettings) = buildString {
        appendLine(
            "# Othello (${gameSetting.darkStrategy.requiredName} vs. ${gameSetting.lightStrategy.requiredName})",
        )
        appendLine(BoardSerializer.toString(BoardFactory.starter()))
    }
}
