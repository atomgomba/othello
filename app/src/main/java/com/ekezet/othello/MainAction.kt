package com.ekezet.othello

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next

sealed interface MainAction : Action<MainModel, Unit> {
    data object OnNewGameClicked : MainAction {
        override fun MainModel.proceed() =
            change(
                copy(
                    gameState = defaultGameState,
                ),
            )
    }

    data object OnToggleIndicatorsClicked : MainAction {
        override fun MainModel.proceed() =
            change(
                copy(
                    displayOptions = displayOptions.copy(
                        showPossibleMoves = !displayOptions.showPossibleMoves,
                    ),
                ),
            )
    }

    data object OnShareBoardClicked : MainAction {
        override fun MainModel.proceed(): Next<MainModel, Unit> {
            TODO("Not yet implemented")
        }
    }
}
