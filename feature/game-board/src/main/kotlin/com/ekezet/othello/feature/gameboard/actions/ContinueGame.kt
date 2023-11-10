package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.feature.gameboard.GameBoardEffect
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.WaitBeforeGameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import com.ekezet.othello.feature.gameboard.WaitBeforePass

internal fun GameBoardModel.nextTurn(newState: OthelloGameState): Next<GameBoardModel, Unit> {
    val effects = mutableListOf<GameBoardEffect>()
    val strategy = if (newState.currentDisk.isLight) lightStrategy else darkStrategy
    val nextMove = strategy?.deriveNext(newState)
    if (strategy != null && nextMove != null) {
        // only wait if next player is not human
        effects.add(WaitBeforeNextTurn(nextMove))
    }
    return ContinueGame.outcome(
        model = resetNextTurn(newState),
        effects = effects.toTypedArray(),
    )
}

internal fun GameBoardModel.passTurn(newState: OthelloGameState): Next<GameBoardModel, Unit> {
    val nextStrategy = if (newState.currentDisk.isLight) lightStrategy else darkStrategy
    val nextMove = nextStrategy?.deriveNext(newState)
    return ContinueGame.outcome(
        model = resetNextTurn(nextState = newState, passed = true),
        WaitBeforePass(nextMove, newState),
    )
}

internal fun GameBoardModel.finishGame(newState: OthelloGameState, winner: Disk?) = ContinueGame.outcome(
    model = resetNextTurn(newState),
    WaitBeforeGameEnd(winner?.let { EndedWin(it) } ?: EndedTie),
)
