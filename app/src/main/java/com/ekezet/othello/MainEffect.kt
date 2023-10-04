package com.ekezet.othello

import android.content.Intent
import com.ekezet.hurok.Effect
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.feature.gameboard.actions.OnSerializeGameState
import com.ekezet.othello.feature.gameboard.actions.OnUpdateGameState

internal sealed interface MainEffect : Effect<MainModel, MainDependency>

internal data class PublishGameSettings(
    private val settings: IGameSettings,
) : MainEffect {
    override suspend fun LoopScope<MainModel, MainDependency>.trigger(
        dependency: MainDependency?,
    ) = dependency?.run {
        gameSettingsStore.update(settings)
    }
}

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
            OnSerializeGameState {
                emit(OnGameStateSerialized(it))
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
        val shareIntent = Intent.createChooser(sendIntent, null).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        androidContext.startActivity(shareIntent)
    }
}
