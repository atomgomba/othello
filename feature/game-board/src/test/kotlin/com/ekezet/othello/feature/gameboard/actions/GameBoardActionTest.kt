package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.OthelloGameState
import com.ekezet.othello.core.game.ValidMove
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.GameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith

@RunWith(JUnitParamsRunner::class)
class GameBoardActionTest {
    @Test
    fun `OnGameStarted works correctly if Dark is HumanPlayer`() {
        val initModel = GameBoardModel()

        initModel after OnGameStarted matches {
            assertSkipped()
        }
    }

    @Test
    @Parameters(method = "paramsForGameEnd")
    fun `OnGameStarted works correctly if game has ended`(ended: GameEnd?) {
        val initModel = GameBoardModel(
            ended = ended!!,
        )

        initModel after OnGameStarted matches {
            assertSkipped()
        }
    }

    @Test
    fun `OnGameStarted works correctly if Dark is NPC`() {
        val nextMove = Position(42, 42)
        val gameState: OthelloGameState = mockk()
        val darkStrategy: Strategy = mockk {
            every { deriveNext(gameState) } returns nextMove
        }

        val initModel = GameBoardModel(
            darkStrategy = darkStrategy,
            gameState = gameState,
        )

        initModel after OnGameStarted matches {
            assertEffects(setOf(WaitBeforeNextTurn(nextMove)))
        }

        verify {
            darkStrategy.deriveNext(gameState)
        }

        confirmVerified(gameState, darkStrategy)
    }

    @Test
    fun `OnGameStarted works correctly if Dark is NPC and cannot move`() {
        val gameState: OthelloGameState = mockk()
        val darkStrategy: Strategy = mockk {
            every { deriveNext(gameState) } returns null
        }

        val initModel = GameBoardModel(
            darkStrategy = darkStrategy,
            gameState = gameState,
        )

        assertFailsWith(IllegalStateException::class) {
            initModel after OnGameStarted
        }

        verify {
            darkStrategy.deriveNext(gameState)
        }

        confirmVerified(gameState, darkStrategy)
    }

    @Test
    fun `OnMoveMade works correctly if move is valid`() {
        val position = Position(42, 42)
        val gameState: OthelloGameState = mockk(relaxed = true) {
            every { validMoves } returns setOf(ValidMove(position, mockk()))
        }

        @Suppress("ReplaceCallWithBinaryOperator")
        every { gameState.equals(gameState) } answers { callOriginal() }

        val initModel = GameBoardModel(
            gameState = gameState,
        )

        val expectedModel = initModel.pickNextMoveAt(position)

        initModel after OnMoveMade(position) matches {
            assertModel(expectedModel)
            assertNoEffects()
        }

        verify {
            gameState.validMoves
            @Suppress("UnusedEquals")
            gameState.equals(gameState)
        }

        confirmVerified(gameState)
    }

    @Test
    fun `OnMoveMade works correctly if move is invalid`() {
        val position = Position(42, 42)

        val initModel = GameBoardModel()

        initModel after OnMoveMade(position) matches {
            assertSkipped()
        }
    }

    @Suppress("unused")
    private fun paramsForGameEnd() = arrayOf(
        arrayOf(GameEnd.EndedWin(winner = Disk.Dark)),
        arrayOf(GameEnd.EndedWin(winner = Disk.Light)),
        arrayOf(GameEnd.EndedTie),
    )
}
