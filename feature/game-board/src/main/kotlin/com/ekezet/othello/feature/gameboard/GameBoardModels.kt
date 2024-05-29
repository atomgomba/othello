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
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultGameState
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.ui.render.HistoryImagesRenderer
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
    internal val gameState: OthelloGameState = defaultGameState,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    internal val nextMovePosition: Position? = null,
    internal val passed: Boolean = false,
    internal val ended: GameEnd? = null,
) : IGameSettings {
    internal val currentDisk: Disk
        inline get() = gameState.currentDisk

    internal val Disk.isHumanPlayer: Boolean
        get() = when (this) {
            Disk.Light -> lightStrategy == HumanPlayer
            Disk.Dark -> darkStrategy == HumanPlayer
            else -> error("Expected Light or Dark, but was $this")
        }

    internal fun resetNextTurn(
        nextState: CurrentGameState = defaultGameState,
        passed: Boolean = false,
    ) = copy(
        gameState = if (passed) nextState.lastState else nextState,
        nextMovePosition = null,
        ended = null,
        passed = passed,
    )

    internal fun pickNextMoveAt(position: Position?) = copy(
        nextMovePosition = position,
    )
}

@Immutable
internal data class GameBoardState(
    val board: BoardList,
    val overlay: BoardOverlayList,
    val currentTurn: Int,
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
    internal val boardBackground: Color
        inline get() = if (displayOptions.isGrayscaleMode) {
            Color(0xFFC0C0C0)
        } else {
            Color(0xFF338033)
        }
}

class GameBoardDependency(
    gameHistoryStore: GameHistoryStore? = null,
    historyImagesRenderer: HistoryImagesRenderer? = null,
) : KoinComponent {
    val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()
    val historyImagesRenderer: HistoryImagesRenderer = historyImagesRenderer ?: get()
}

typealias GameBoardEmitter = ActionEmitter<GameBoardModel, GameBoardDependency>
