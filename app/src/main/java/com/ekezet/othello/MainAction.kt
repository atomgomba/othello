package com.ekezet.othello

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.MainEffect.SerializeBoard
import com.ekezet.othello.MainEffect.StartShareIntent
import com.ekezet.othello.MainEffect.UpdateGameBoardGameState
import com.ekezet.othello.core.game.GameState
import timber.log.Timber

internal sealed interface MainAction : Action<MainModel, MainDependency> {
    data object OnNewGameClicked : MainAction {
        override fun MainModel.proceed() =
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
        override fun MainModel.proceed() = trigger(SerializeBoard)
    }

    data class OnBoardSerialized(
        private val lines: List<String>
    ) : MainAction {
        override fun MainModel.proceed(): Next<MainModel, MainDependency> {
            val debugLines = lines.joinToString(",\n") { "\"$it\"" }
            Timber.d("Board:\n$debugLines")
            val text = if (BuildConfig.DEBUG) {
                debugLines
            } else {
                lines.joinToString("\n")
            }
            return trigger(StartShareIntent(text))
        }
    }
}
