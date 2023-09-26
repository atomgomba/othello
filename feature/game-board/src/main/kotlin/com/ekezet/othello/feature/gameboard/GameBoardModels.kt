package com.ekezet.othello.feature.gameboard

import androidx.compose.runtime.Stable
import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.Strategy
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList

/**
 * Give a little time for humans to follow changes on the board
 */
const val MOVE_DELAY_MILLIS = 300L

internal val defaultBoard: Board
    get() = BoardSerializer.fromLines(
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
    get() = GameState.new(defaultBoard)

internal val defaultArgs: GameBoardArgs
    inline get() = GameBoardArgs(
        gameState = defaultGameState,
        displayOptions = DisplayOptions(),
        opponentStrategy = NaiveMaxStrategy(),
    )

data class GameBoardArgs(
    val gameState: GameState,
    val displayOptions: DisplayOptions,
    val opponentStrategy: Strategy?,
)

data class DisplayOptions(
    val showPossibleMoves: Boolean = true,
    val showBoardPositions: Boolean = false,
)

internal data class GameBoardModel(
    val gameState: GameState = defaultGameState,
    val displayOptions: DisplayOptions = DisplayOptions(),
    val nextMovePosition: Position? = null,
    val opponentStrategy: Strategy? = null,
) {
    val currentDisk: Disk
        get() = gameState.currentDisk

    val diskCount: DiskCount by lazy {
        gameState.currentBoard
            .flatten()
            .filterNotNull()
            .fold(DiskCount(0, 0)) { acc, disk ->
                DiskCount(
                    first = if (disk.isDark) acc.first + 1 else acc.first,
                    second = if (disk.isLight) acc.second + 1 else acc.second,
                )
            }
    }
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
    val hasPossibleMoves: Boolean,
    val showPossibleMoves: Boolean,
    val showBoardPositions: Boolean,
    val onCellClick: (x: Int, y: Int) -> Unit,
    val onResetGameClick: () -> Unit,
    val onToggleIndicatorsClick: () -> Unit,
)

internal object GameBoardDependency

internal typealias DiskCount = Pair<Int, Int>

internal val DiskCount.numDark: Int
    inline get() = first

internal val DiskCount.numLight: Int
    inline get() = second
