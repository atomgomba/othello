package com.ekezet.othello.feature.gameboard

import androidx.compose.runtime.Stable
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.DiskCount
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList

val defaultBoard: Board
    inline get() = BoardSerializer.fromString(
        """
        --------
        --------
        --------
        ---ox---
        ---xo---
        --------
        --------
        --------
        """,
    )

val defaultGameState: GameState
    inline get() = GameState.new(defaultBoard)

val defaultDisplayOptions: BoardDisplayOptions
    inline get() = BoardDisplayOptions(
        showPossibleMoves = true,
        showBoardPositions = false,
    )

val defaultLightStrategy: Strategy = NaiveMaxStrategy().preferSides()

val defaultDarkStrategy: Strategy? = HumanPlayer

val defaultGameBoardArgs: GameBoardArgs
    inline get() = GameBoardArgs(
        displayOptions = defaultDisplayOptions,
        lightStrategy = defaultLightStrategy,
        darkStrategy = defaultDarkStrategy,
    )

/**
 * Give a little time for humans to follow changes on the board
 */
internal const val ACTION_DELAY_MILLIS = 300L

data class GameBoardArgs(
    override val displayOptions: BoardDisplayOptions,
    override val lightStrategy: Strategy?,
    override val darkStrategy: Strategy?,
) : GameSettings

data class GameBoardModel(
    internal val gameState: GameState = defaultGameState,
    internal val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
    internal val lightStrategy: Strategy? = defaultLightStrategy,
    internal val darkStrategy: Strategy? = defaultDarkStrategy,
    internal val nextMovePosition: Position? = null,
    internal val ended: GameEnd? = null,
) {
    internal val currentDisk: Disk
        inline get() = gameState.currentDisk

    internal fun isHumanPlayer(disk: Disk): Boolean =
        when (disk) {
            Disk.Light -> lightStrategy == HumanPlayer
            Disk.Dark -> darkStrategy == HumanPlayer
            else -> error("Expected Light or Dark, but was $disk")
        }

    internal fun resetNextTurn(nextState: GameState = defaultGameState) =
        copy(
            gameState = nextState,
            nextMovePosition = null,
            ended = null,
        )
}

@Stable
internal data class GameBoardState(
    val board: BoardList,
    val overlay: BoardOverlayList,
    val currentTurn: Int,
    val currentDisk: Disk,
    val opponentName: String?,
    val diskCount: DiskCount,
    val nextMovePosition: Position?,
    val showPossibleMoves: Boolean,
    val showBoardPositions: Boolean,
    val ended: GameEnd?,
    val celebrate: Boolean,
    val isHumanPlayer: Boolean,
    val onCellClick: (x: Int, y: Int) -> Unit,
)

typealias GameBoardScope = LoopScope<GameBoardModel, Unit>

sealed interface GameEnd {
    data class EndedWin(val winner: Disk) : GameEnd
    data object EndedTie : GameEnd
}
