package com.ekezet.othello.feature.gameboard

import com.ekezet.hurok.test.EffectTest
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.feature.gameboard.actions.OnGameEnded
import com.ekezet.othello.feature.gameboard.actions.OnMoveMade
import com.ekezet.othello.feature.gameboard.actions.OnTurnPassed
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GameBoardEffectTest : EffectTest() {
    @Test
    fun `WaitBeforeNextTurn works correctly`() = runTest {
        val nextMove: Position = mockk()

        Unit runWith WaitBeforeNextTurn(nextMove) matches {
            assertActions(listOf(OnMoveMade(nextMove)))
        }
    }

    @Test
    fun `WaitBeforePassTurn works correctly`() = runTest {
        val nextMove: Position = mockk()
        val newState: OthelloGameState = mockk()

        Unit runWith WaitBeforePassTurn(nextMove, newState) matches {
            assertActions(listOf(OnTurnPassed(nextMove, newState)))
        }
    }

    @Test
    fun `WaitBeforeGameEnd works correctly`() = runTest {
        val result: GameEnd = mockk()

        Unit runWith WaitBeforeGameEnd(result) matches {
            assertActions(listOf(OnGameEnded(result)))
        }
    }
}
