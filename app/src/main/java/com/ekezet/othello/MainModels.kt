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
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named

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
    gameBoardScope: GameBoardScope? = null
) : KoinComponent {
    val gameBoardScope: GameBoardScope = gameBoardScope ?: get(named("gameBoardScope"))
}

internal typealias MainScope = LoopScope<MainModel, MainDependency>
