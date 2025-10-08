package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.GameEnd.EndedTie
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.feature.gameboard.GameBoardDependency
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.PublishPastMoves
import com.ekezet.othello.feature.gameboard.WaitBeforeGameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import com.ekezet.othello.feature.gameboard.WaitBeforePassTurn

internal fun GameBoardModel.nextTurn(newState: CurrentGameState): Next<GameBoardModel, GameBoardDependency> {
    val strategy = if (newState.currentDisk.isLight) lightStrategy else darkStrategy
    val nextMove = strategy?.deriveNext(newState)
    val effects = buildList {
        add(PublishPastMoves(newState, ended))
        if (strategy != HumanPlayer && nextMove != null) {
            // only wait if next player is not human
            add(WaitBeforeNextTurn(nextMove))
        }
    }
    return ContinueGame.next(
        model = resetNextTurn(newState),
        effects = effects.toTypedArray(),
    )
}

internal fun GameBoardModel.passTurn(newState: CurrentGameState): Next<GameBoardModel, GameBoardDependency> {
    val nextStrategy = if (newState.currentDisk.isLight) lightStrategy else darkStrategy
    val nextMove = nextStrategy?.deriveNext(newState)
    return ContinueGame.next(
        model = resetNextTurn(newState, passed = true),
        PublishPastMoves(newState),
        WaitBeforePassTurn(nextMove, newState),
    )
}

internal fun GameBoardModel.finishGame(newState: CurrentGameState, winner: Disk?): Next<GameBoardModel, GameBoardDependency> {
    val gameEnd = winner?.let { EndedWin(it) } ?: EndedTie
    return ContinueGame.next(
        model = resetNextTurn(newState),
        PublishPastMoves(newState, gameEnd),
        WaitBeforeGameEnd(gameEnd),
    )
}
