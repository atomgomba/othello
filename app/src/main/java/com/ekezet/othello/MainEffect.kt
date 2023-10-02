package com.ekezet.othello

import android.content.Intent
import com.ekezet.hurok.Effect
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.feature.gameboard.actions.OnSerializeBoard
import com.ekezet.othello.feature.gameboard.actions.OnUpdateGameState

internal sealed interface MainEffect : Effect<MainModel, MainDependency>

internal data class UpdateGameBoardGameState(
    private val newState: GameState,
) : MainEffect {
    override suspend fun LoopScope<MainModel, MainDependency>.trigger(
        dependency: MainDependency?,
    ) = dependency?.run {
        gameBoardScope?.emit(OnUpdateGameState(newState))
    }
}

internal data object SerializeBoard : MainEffect {
    override suspend fun LoopScope<MainModel, MainDependency>.trigger(
        dependency: MainDependency?,
    ) = dependency?.run {
        gameBoardScope?.emit(
            OnSerializeBoard {
                emit(OnGameSerialized(it))
            },
        )
    }
}

internal data class StartShareIntent(
    private val text: String,
) : MainEffect {
    override suspend fun LoopScope<MainModel, MainDependency>.trigger(
        dependency: MainDependency?,
    ) = dependency?.run {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        mainActivity.startActivity(shareIntent)
    }
}
