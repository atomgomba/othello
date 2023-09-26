package com.ekezet.othello.feature.gameboard

import androidx.compose.runtime.Stable
import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList

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
    inline get() = GameBoardArgs(gameState = defaultGameState)

internal data class DisplayOptions(
    val showPossibleMoves: Boolean = true,
    val showBoardPositions: Boolean = false,
)

internal data class GameBoardArgs(
    val gameState: GameState,
    val displayOptions: DisplayOptions = DisplayOptions(),
)

internal data class GameBoardModel(
    val gameState: GameState = defaultGameState,
    val displayOptions: DisplayOptions = DisplayOptions(),
    val nextMovePosition: Position? = null,
) {
    val currentDisk: Disk
        get() = gameState.currentDisk
}

@Stable
internal data class GameBoardState(
    val board: BoardList,
    val overlay: BoardOverlayList,
    val currentTurn: Int,
    val currentDisk: Disk,
    val nextMovePosition: Position?,
    val hasPossibleMoves: Boolean,
    val onCellClick: (x: Int, y: Int) -> Unit,
    val onResetGameClick: () -> Unit,
    val onToggleIndicatorsClick: () -> Unit,
    val showPossibleMoves: Boolean,
    val showBoardPositions: Boolean,
)
