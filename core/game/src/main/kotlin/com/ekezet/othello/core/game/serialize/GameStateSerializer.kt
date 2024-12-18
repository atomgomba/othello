package com.ekezet.othello.core.game.serialize

import com.ekezet.othello.core.data.models.diskCount
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.data.serialize.asString
import com.ekezet.othello.core.game.BoardFactory
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.strategy.requiredName

object GameStateSerializer {
    fun toString(gameState: CurrentGameState, gameSettings: IGameSettings) = buildString {
        appendLine(serializedHeader(gameSettings))
        for (pastMove in gameState.pastMoves) {
            appendLine(pastMoveToString(pastMove))
        }
        val lastMove = gameState.pastMoves.lastOrNull() ?: return@buildString
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

    fun fromString(ignored: String): CurrentGameState {
        TODO()
    }

    private fun serializedHeader(gameSetting: IGameSettings) = buildString {
        appendLine(
            "# Othello (${gameSetting.darkStrategy.requiredName} vs. ${gameSetting.lightStrategy.requiredName})",
        )
        appendLine(BoardSerializer.toString(BoardFactory.starter()))
    }
}
