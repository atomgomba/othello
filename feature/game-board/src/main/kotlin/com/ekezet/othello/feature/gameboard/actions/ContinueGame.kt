package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.feature.gameboard.GameBoardDependency
import com.ekezet.othello.feature.gameboard.GameBoardEffect
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameEnd.EndedTie
import com.ekezet.othello.feature.gameboard.GameEnd.EndedWin
import com.ekezet.othello.feature.gameboard.PublishPastMove
import com.ekezet.othello.feature.gameboard.WaitBeforeGameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import com.ekezet.othello.feature.gameboard.WaitBeforePassTurn

internal fun GameBoardModel.nextTurn(newState: CurrentGameState): Next<GameBoardModel, GameBoardDependency> {
    val effects = mutableListOf<GameBoardEffect>(
        PublishPastMove(newState.history.last())
    )
    val strategy = if (newState.currentDisk.isLight) lightStrategy else darkStrategy
    val nextMove = strategy?.deriveNext(newState)
    if (strategy != HumanPlayer && nextMove != null) {
        // only wait if next player is not human
        effects.add(WaitBeforeNextTurn(nextMove))
    }
    return ContinueGame.outcome(
        model = resetNextTurn(newState),
        effects = effects.toTypedArray(),
    )
}

internal fun GameBoardModel.passTurn(newState: CurrentGameState): Next<GameBoardModel, GameBoardDependency> {
    val nextStrategy = if (newState.currentDisk.isLight) lightStrategy else darkStrategy
    val nextMove = nextStrategy?.deriveNext(newState)
    return ContinueGame.outcome(
        model = resetNextTurn(newState, passed = true),
        PublishPastMove(newState.history.last()),
        WaitBeforePassTurn(nextMove, newState),
    )
}

internal fun GameBoardModel.finishGame(newState: CurrentGameState, winner: Disk?) =
    ContinueGame.outcome(
        model = resetNextTurn(newState),
        PublishPastMove(newState.history.last()),
        WaitBeforeGameEnd(winner?.let { EndedWin(it) } ?: EndedTie),
    )
