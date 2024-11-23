package com.ekezet.othello.feature.gameboard

import androidx.annotation.IntRange
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
import com.ekezet.othello.core.game.data.DefaultConfirmExit
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

data class GameBoardModel(
    internal val gameState: OthelloGameState = OthelloGameState.Start,
    @IntRange(from = 0) internal val currentTurn: Int = 0,
    override val boardDisplayOptions: BoardDisplayOptions = BoardDisplayOptions.Default,
    override val lightStrategy: Strategy? = DefaultLightStrategy,
    override val darkStrategy: Strategy? = DefaultDarkStrategy,
    override val confirmExit: Boolean = DefaultConfirmExit,
    internal val nextMovePosition: Position? = null,
    internal val passed: Boolean = false,
    internal val ended: GameEnd? = null,
) : IGameSettings {
    internal val maxTurnCount: Int
        inline get() = gameState.turn

    internal val currentGameState: OthelloGameState
        inline get() = if (currentTurn == maxTurnCount) {
            gameState
        } else {
            gameState.history.toPastGameState(0, currentTurn)
        }

    internal val currentDisk: Disk
        inline get() = currentGameState.currentDisk

    internal val Disk.isHumanPlayer: Boolean
        inline get() = when (this) {
            Disk.Light -> lightStrategy == HumanPlayer
            Disk.Dark -> darkStrategy == HumanPlayer
            else -> error("Expected Light or Dark, but was $this")
        }

    internal fun resetNextTurn(
        nextState: OthelloGameState = OthelloGameState.Start,
        passed: Boolean = false,
    ) = copy(
        gameState = if (passed) nextState.lastState else nextState,
        currentTurn = if (nextState == OthelloGameState.Start) 0 else currentTurn + 1,
        nextMovePosition = null,
        ended = null,
        passed = passed,
    )

    internal fun pickNextMoveAt(position: Position?) = copy(
        nextMovePosition = position,
    )

    internal fun stepToPreviousTurn() = if (0 < currentTurn) {
        copy(currentTurn = currentTurn - 1)
    } else {
        this
    }

    internal fun stepToNextTurn() = if (currentTurn < maxTurnCount) {
        copy(currentTurn = currentTurn + 1)
    } else {
        this
    }
}

@Immutable
internal data class GameBoardState(
    val board: BoardList,
    val overlay: BoardOverlayList,
    @IntRange(from = 1) val displayedTurn: Int,
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
) : ViewState<GameBoardModel, GameBoardDependency>() {
    val hasPreviousTurn: Boolean
        inline get() = 1 < displayedTurn

    val boardBackground: Color
        inline get() = if (displayOptions.isGrayscaleMode) {
            BoardBackgroundGrayscale
        } else {
            BoardBackground
        }
}

class GameBoardDependency(
    gameHistoryStore: GameHistoryStore? = null,
    movesRenderer: MovesRenderer? = null,
) : KoinComponent {
    val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()
    val movesRenderer: MovesRenderer = movesRenderer ?: get()
}

typealias GameBoardEmitter = ActionEmitter<GameBoardModel, GameBoardDependency>
