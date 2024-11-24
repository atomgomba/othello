package com.ekezet.othello.feature.gameboard

import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.DiskCount
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultDarkStrategy
import com.ekezet.othello.core.game.data.DefaultLightStrategy
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.Start
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.toPastGameState
import com.ekezet.othello.core.ui.render.MovesRenderer
import com.ekezet.othello.core.ui.theme.BoardBackground
import com.ekezet.othello.core.ui.theme.BoardBackgroundGrayscale
import com.ekezet.othello.core.ui.viewModels.BoardList
import com.ekezet.othello.feature.gameboard.ui.viewModels.BoardOverlayList
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/**
 * Give a little time for humans to follow changes on the board
 */
internal const val ACTION_DELAY_MILLIS: Long = DefaultDurationMillis
    .times(1.5F)
    .toLong()

@ConsistentCopyVisibility
data class GameBoardModel internal constructor(
    private val gameState: OthelloGameState = OthelloGameState.Start,
    internal val selectedTurn: Int = 0,
    override val boardDisplayOptions: BoardDisplayOptions = BoardDisplayOptions.Default,
    override val lightStrategy: Strategy? = DefaultLightStrategy,
    override val darkStrategy: Strategy? = DefaultDarkStrategy,
    internal val nextMovePosition: Position? = null,
    internal val passed: Boolean = false,
    internal val ended: GameEnd? = null,
) : IGameSettings {
    internal val turnCount: Int
        inline get() = gameState.turn

    internal val isCurrentTurn: Boolean
        inline get() = selectedTurn == turnCount

    internal val currentGameState: OthelloGameState by lazy {
        if (isCurrentTurn) {
            gameState
        } else {
            val toIndex = (selectedTurn + 1).coerceAtMost(turnCount)
            gameState.history.toPastGameState(0, toIndex)
        }
    }

    internal val currentDisk: Disk
        inline get() = currentGameState.currentDisk

    internal val Disk.isHumanPlayer: Boolean
        inline get() = when (this) {
            Disk.Light -> lightStrategy == HumanPlayer
            Disk.Dark -> darkStrategy == HumanPlayer
        }

    internal fun resetNewGame(
        state: OthelloGameState = OthelloGameState.Start,
    ) = copy(
        gameState = state,
        selectedTurn = 0,
        passed = false,
        nextMovePosition = null,
        ended = null,
    )

    internal fun resetNextTurn(
        nextState: OthelloGameState,
        passed: Boolean = false,
    ) = copy(
        gameState = if (passed) nextState.lastState else nextState,
        selectedTurn = selectedTurn + 1,
        passed = passed,
        nextMovePosition = null,
    )

    internal fun pickNextMoveAt(position: Position?) = copy(
        nextMovePosition = position,
    )

    internal fun stepToPreviousTurn() = if (0 < selectedTurn) {
        copy(selectedTurn = selectedTurn - 1)
    } else {
        this
    }

    internal fun stepToNextTurn() = if (selectedTurn < turnCount) {
        copy(selectedTurn = selectedTurn + 1)
    } else {
        this
    }
}

@Immutable
internal data class GameBoardState(
    val board: BoardList,
    val overlay: BoardOverlayList,
    val displayedTurn: Int,
    val displayedMaxTurnCount: Int,
    val hasNextTurn: Boolean,
    val currentDisk: Disk,
    val darkStrategyName: String?,
    val lightStrategyName: String?,
    val opponentName: String?,
    val diskCount: DiskCount,
    val nextMovePosition: Position?,
    val displayOptions: BoardDisplayOptions,
    val ended: GameEnd?,
    val celebrate: Boolean,
    val isHumanPlayer: Boolean,
    val passed: Boolean,
    val isCurrentTurn: Boolean,
) : ViewState<GameBoardModel, GameBoardDependency>() {
    val hasPreviousTurn: Boolean
        inline get() = 1 < displayedTurn

    val boardBackground: Color
        inline get() = if (displayOptions.isGrayscaleMode) {
            BoardBackgroundGrayscale
        } else {
            BoardBackground
        }

    val isBoardClickable: Boolean
        inline get() = isHumanPlayer && isCurrentTurn
}

data class GameBoardArgs(
    val selectedTurn: Int?,
    override val boardDisplayOptions: BoardDisplayOptions,
    override val lightStrategy: Strategy?,
    override val darkStrategy: Strategy?,
) : IGameSettings

class GameBoardDependency(
    gameHistoryStore: GameHistoryStore? = null,
    movesRenderer: MovesRenderer? = null,
) : KoinComponent {
    internal val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()
    internal val movesRenderer: MovesRenderer = movesRenderer ?: get()
}

typealias GameBoardEmitter = ActionEmitter<GameBoardModel, GameBoardDependency>
