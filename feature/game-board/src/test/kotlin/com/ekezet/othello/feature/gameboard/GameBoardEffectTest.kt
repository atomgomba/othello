package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.test.EffectTest
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.ui.render.MovesRenderer
import com.ekezet.othello.feature.gameboard.actions.OnGameEnded
import com.ekezet.othello.feature.gameboard.actions.OnMoveMade
import com.ekezet.othello.feature.gameboard.actions.OnTurnPassed
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GameBoardEffectTest : EffectTest() {
    @MockK
    private lateinit var mockGameHistoryStore: GameHistoryStore

    @MockK
    private lateinit var mockMovesRenderer: MovesRenderer

    private lateinit var dependency: GameBoardDependency

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        dependency = GameBoardDependency(
            gameHistoryStore = mockGameHistoryStore,
            movesRenderer = mockMovesRenderer,
        )
    }

    @Test
    fun `WaitBeforeNextTurn works correctly`() = runTest {
        val nextMove: Position = mockk()

        dependency runWith WaitBeforeNextTurn(nextMove) matches {
            assertActions(listOf(OnMoveMade(nextMove)))
        }
    }

    @Test
    fun `WaitBeforePassTurn works correctly`() = runTest {
        val nextMove: Position = mockk()
        val newState: CurrentGameState = mockk()

        dependency runWith WaitBeforePassTurn(nextMove, newState) matches {
            assertActions(listOf(OnTurnPassed(nextMove, newState)))
        }
    }

    @Test
    fun `WaitBeforeGameEnd works correctly`() = runTest {
        val result: GameEnd = mockk()

        dependency runWith WaitBeforeGameEnd(result) matches {
            assertActions(listOf(OnGameEnded(result)))
        }
    }
}
