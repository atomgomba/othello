package com.ekezet.othello

import androidx.compose.runtime.Stable
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.DisplayOptions
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.data.GameSettings
import com.ekezet.othello.feature.gameboard.defaultDisplayOptions
import com.ekezet.othello.feature.gameboard.di.GameBoardScopeName
import com.ekezet.othello.ui.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val opponentStrategy: Strategy? = NaiveMaxStrategy(),
    override val displayOptions: DisplayOptions = defaultDisplayOptions,
) : GameSettings

@Stable
internal data class MainState(
    val gameSettings: GameSettings,
    val gameBoardArgs: GameBoardArgs,
    val onNewGameClick: () -> Unit,
    val onToggleIndicatorsClick: () -> Unit,
)

internal class MainDependency(
    mainActivity: MainActivity? = null,
    gameBoardScope: GameBoardScope? = null,
) : KoinComponent {
    val mainActivity: MainActivity = mainActivity ?: get()
    val gameBoardScope: GameBoardScope = gameBoardScope ?: get(GameBoardScopeName)
}

internal typealias MainScope = LoopScope<MainModel, MainDependency>
