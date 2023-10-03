package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.NaiveMaxStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy

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

val defaultDarkStrategy: Strategy? = HumanPlayer

val defaultLightStrategy: Strategy = NaiveMaxStrategy.preferSides()
