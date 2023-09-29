package com.ekezet.othello

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.MainEffect.UpdateGameBoardGameState
import com.ekezet.othello.core.game.GameState

internal sealed interface MainAction : Action<MainModel, MainDependency> {
    data object OnNewGameClicked : MainAction {
        override fun MainModel.proceed(): Next<MainModel, MainDependency> =
            trigger(UpdateGameBoardGameState(GameState.new()))
    }

    data object OnToggleIndicatorsClicked : MainAction {
        override fun MainModel.proceed(): Next<MainModel, MainDependency> {
            val newSettings = copy(
                displayOptions = displayOptions.copy(
                    showPossibleMoves = !displayOptions.showPossibleMoves,
                ),
            )
            return outcome(newSettings)
        }
    }

    data object OnShareBoardClicked : MainAction {
        override fun MainModel.proceed(): Next<MainModel, MainDependency> {
            TODO("Not yet implemented")
        }
    }
}
