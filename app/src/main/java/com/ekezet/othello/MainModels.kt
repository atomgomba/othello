package com.ekezet.othello

import androidx.compose.runtime.Stable
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gameboard.defaultDisplayOptions
import com.ekezet.othello.ui.MainActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class MainModel(
    override val darkStrategy: Strategy? = HumanPlayer,
    override val lightStrategy: Strategy? = NaiveMaxStrategy().preferSides(),
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
) : GameSettings

@Stable
internal data class MainState(
    val gameSettings: GameSettings,
    val gameBoardArgs: GameBoardArgs,
    val onNewGameClick: () -> Unit,
    val onToggleIndicatorsClick: () -> Unit,
    val onShareGameClick: () -> Unit
)

internal class MainDependency(
    mainActivity: MainActivity? = null,
) : KoinComponent {
    val mainActivity: MainActivity = mainActivity ?: get()
    var gameBoardScope: GameBoardScope? = null
        internal set
}

internal typealias MainScope = LoopScope<MainModel, MainDependency>
