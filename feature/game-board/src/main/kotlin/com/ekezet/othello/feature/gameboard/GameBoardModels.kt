package com.ekezet.othello.feature.gameboard

import androidx.compose.runtime.Stable
import com.ekezet.hurok.LoopScope
import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.data.GameSettings
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList

/**
 * Give a little time for humans to follow changes on the board
 */
const val MOVE_DELAY_MILLIS = 300L

val defaultBoard: Board
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

val defaultGameState: GameState
    inline get() = GameState.new(defaultBoard)

val defaultDisplayOptions: DisplayOptions
    inline get() = DisplayOptions(
        showPossibleMoves = true,
        showBoardPositions = false,
    )

val defaultStrategy: Strategy? = HumanPlayer

val defaultGameBoardArgs: GameBoardArgs
    inline get() = GameBoardArgs(
        displayOptions = defaultDisplayOptions,
        opponentStrategy = defaultStrategy,
    )

data class GameBoardArgs(
    override val displayOptions: DisplayOptions,
    override val opponentStrategy: Strategy?,
) : GameSettings

data class DisplayOptions(
    val showPossibleMoves: Boolean,
    val showBoardPositions: Boolean,
)

data class GameBoardModel(
    val gameState: GameState = defaultGameState,
    val displayOptions: DisplayOptions = defaultDisplayOptions,
    val opponentStrategy: Strategy? = defaultStrategy,
    val nextMovePosition: Position? = null,
) {
    val currentDisk: Disk
        inline get() = gameState.currentDisk

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

    companion object {
        fun fromArgs(args: GameBoardArgs) = with(args) {
            GameBoardModel(
                opponentStrategy = opponentStrategy,
                displayOptions = displayOptions,
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
)

internal typealias DiskCount = Pair<Int, Int>

internal val DiskCount.numDark: Int
    inline get() = first

internal val DiskCount.numLight: Int
    inline get() = second

typealias GameBoardScope = LoopScope<GameBoardModel, Unit>
