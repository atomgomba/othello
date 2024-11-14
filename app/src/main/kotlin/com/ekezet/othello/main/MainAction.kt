package com.ekezet.othello.main

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.state.OthelloGameState

internal sealed interface MainAction : Action<MainModel, MainDependency>

internal data object OnNewGameClicked : MainAction {
    override fun MainModel.proceed() =
        trigger(UpdateGameBoardGameState(OthelloGameState.Default), ResetPastMoves)
}

internal data object OnToggleIndicatorsClicked : MainAction {
    override fun MainModel.proceed(): Next<MainModel, MainDependency> {
        val newSettings = copy(
            boardDisplayOptions = boardDisplayOptions.copy(
                showPossibleMoves = !boardDisplayOptions.showPossibleMoves,
            ),
        )
        return outcome(newSettings, PublishGameSettings(newSettings))
    }
}

internal data object OnBackPressed : MainAction {
    override fun MainModel.proceed(): Next<MainModel, MainDependency> {
        val effects = buildSet<MainEffect> {
            if (isExitMessageVisible) {
                add(FinishActivity)
            }
        }
        return outcome(
            model = copy(backPressCount = backPressCount + 1),
            effects = effects.toTypedArray(),
        )
    }
}

internal data object OnCancelExitClicked : MainAction {
    override fun MainModel.proceed() = mutate(copy(backPressCount = 0))
}
