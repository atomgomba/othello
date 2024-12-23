package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.state.OthelloGameState
import com.ekezet.othello.core.game.state.new
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy

val StartBoard: Board
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

val OthelloGameState.Companion.Start: OthelloGameState
    inline get() = CurrentGameState.new(StartBoard)

val BoardDisplayOptions.Companion.Default: BoardDisplayOptions
    inline get() = BoardDisplayOptions(
        showPossibleMoves = false,
        showBoardPositions = false,
        isGrayscaleMode = false,
    )

val HistoryDisplayOptions.Companion.Default: HistoryDisplayOptions
    inline get() = HistoryDisplayOptions(
        alwaysScrollToBottom = true,
    )

val DefaultDarkStrategy: Strategy? = HumanPlayer

val DefaultLightStrategy: Strategy = NaiveMaxStrategy.preferSides()

const val DefaultConfirmExit = true

val GameSettings.Companion.Default: GameSettings
    inline get() = GameSettings(
        boardDisplayOptions = BoardDisplayOptions.Default,
        lightStrategy = DefaultLightStrategy,
        darkStrategy = DefaultDarkStrategy,
    )

val HistorySettings.Companion.Default: HistorySettings
    inline get() = HistorySettings(
        historyDisplayOptions = HistoryDisplayOptions.Default,
        showHistoryAsText = false,
    )

val AppSettings.Companion.Default: AppSettings
    inline get() = AppSettings(
        confirmExit = DefaultConfirmExit,
    )
