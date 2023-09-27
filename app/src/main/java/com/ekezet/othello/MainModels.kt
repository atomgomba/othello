package com.ekezet.othello

import androidx.compose.runtime.Stable
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.DisplayOptions
import com.ekezet.othello.feature.gameboard.GameBoardArgs
import com.ekezet.othello.feature.gameboard.data.GameSettings

internal val defaultBoard: Board
    inline get() = BoardSerializer.fromLines(
        ",,,,,,,,",
        ",,,,,,,,",
        ",,,,,,,,",
        ",,,ox,,,",
        ",,,xo,,,",
        ",,,,,,,,",
        ",,,,,,,,",
        ",,,,,,,,",
    )

internal val defaultGameState: GameState
    inline get() = GameState.new(defaultBoard)

internal val defaultDisplayOptions: DisplayOptions
    inline get() = DisplayOptions(
        showPossibleMoves = true,
        showBoardPositions = false,
    )

data class MainModel(
    override val gameState: GameState = defaultGameState,
    override val opponentStrategy: Strategy? = NaiveMaxStrategy(),
    override val displayOptions: DisplayOptions = defaultDisplayOptions,
) : GameSettings

@Stable
data class MainState(
    val gameSettings: GameSettings,
    val gameBoardArgs: GameBoardArgs,
    val onNewGameClick: () -> Unit,
    val onToggleIndicatorsClick: () -> Unit,
)

typealias MainScope = LoopScope<MainModel, Unit>
