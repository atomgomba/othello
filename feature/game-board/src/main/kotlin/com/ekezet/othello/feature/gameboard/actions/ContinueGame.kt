package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.feature.gameboard.GameBoardEffect
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.WaitBeforeGameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import com.ekezet.othello.feature.gameboard.WaitBeforePass

internal fun GameBoardModel.nextTurn(newState: GameState): Next<GameBoardModel, Unit> {
    val effects = mutableListOf<GameBoardEffect>()
    val strategy = if (newState.currentDisk == Disk.Light) lightStrategy else darkStrategy
    if (strategy != null) {
        val nextMove = strategy.deriveNext(newState)
        if (nextMove != null) {
            effects.add(WaitBeforeNextTurn(nextMove))
        }
    }
    return ContinueGame.outcome(
        model = resetNextTurn(newState),
        effects = effects.toTypedArray(),
    )
}

internal fun GameBoardModel.passTurn(newState: GameState): Next<GameBoardModel, Unit> {
    val strategy = if (newState.currentDisk == Disk.Light) lightStrategy else darkStrategy
    val nextMove = strategy?.deriveNext(newState)
    return ContinueGame.trigger(WaitBeforePass(nextMove, newState))
}

internal fun GameBoardModel.finishGame(newState: GameState, winner: Disk?) = ContinueGame.outcome(
    model = resetNextTurn(newState),
    WaitBeforeGameEnd(winner?.let { EndedWin(it) } ?: EndedTie),
)
