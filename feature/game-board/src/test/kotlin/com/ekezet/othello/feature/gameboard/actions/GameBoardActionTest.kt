package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.flip
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameEnd.EndedTie
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.ValidMove
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.strategy.HumanPlayer
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.throwable.InvalidNewMoveException
import com.ekezet.othello.feature.gameboard.GameBoardModel
import com.ekezet.othello.feature.gameboard.PublishPastMoves
import com.ekezet.othello.feature.gameboard.WaitBeforeGameEnd
import com.ekezet.othello.feature.gameboard.WaitBeforeNextTurn
import com.ekezet.othello.feature.gameboard.WaitBeforePassTurn
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertFailsWith

@RunWith(JUnitParamsRunner::class)
internal class GameBoardActionTest {
    private val mockLightStrategy: Strategy = mockk()

    private val testModel = GameBoardModel(
        lightStrategy = mockLightStrategy,
    )

    @Test
    fun `OnGameStarted works correctly if Dark is HumanPlayer`() {
        val initModel = testModel

        initModel after OnGameStarted matches {
            assertSkipped()
        }
    }

    @Test
    @Parameters(method = "paramsForGameEnd")
    fun `OnGameStarted works correctly if game has ended`(ended: GameEnd?) {
        val initModel = testModel.copy(
            ended = ended!!,
        )

        initModel after OnGameStarted matches {
            assertSkipped()
        }
    }

    @Test
    fun `OnGameStarted works correctly if Dark is NPC`() {
        val nextMove = Position(42, 42)
        val gameState: CurrentGameState = mockk()
        val darkStrategy: Strategy = mockk {
            every { deriveNext(gameState) } returns nextMove
        }

        val initModel = testModel.copy(
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
        val gameState: CurrentGameState = mockk()
        val darkStrategy: Strategy = mockk {
            every { deriveNext(gameState) } returns null
        }

        val initModel = testModel.copy(
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
        val position = Position(1, 2)
        val gameState: CurrentGameState = mockk {
            every { validMoves } returns setOf(ValidMove(position, mockk()))
        }

        val initModel = testModel.copy(
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

        val initModel = testModel

        initModel after OnMoveMade(position) matches {
            assertSkipped()
        }
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if move is invalid`(disk: Disk?) {
        val nextMovePosition = Position(2, 3)
        val gameState: CurrentGameState = mockk {
            every { proceed(any()) } throws InvalidNewMoveException(disk!!, nextMovePosition)
            every { currentDisk } returns disk
        }

        val initModel = testModel.copy(
            gameState = gameState,
            nextMovePosition = nextMovePosition,
        )

        initModel after ContinueGame matches {
            assertSkipped()
        }

        verify {
            gameState.proceed(nextMovePosition)
        }

        confirmVerified(gameState)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if next turn`(disk: Disk?) {
        val nextMovePosition = Position(1, 1)
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.flip()
        }
        val currentMovePosition = Position(2, 3)
        val gameState: CurrentGameState = mockk {
            every { proceed(any()) } returns NextTurn(nextState)
            every { currentDisk } returns disk!!
        }

        every { mockLightStrategy.deriveNext(nextState) } returns nextMovePosition

        val initModel = testModel.copy(
            gameState = gameState,
            nextMovePosition = currentMovePosition,
        )

        val expectedModel = initModel.resetNextTurn(nextState)

        initModel after ContinueGame matches {
            assertModel(expectedModel)

            if (disk!!.isDark && expectedModel.darkStrategy == HumanPlayer) {
                assertEffects(setOf(WaitBeforeNextTurn(nextMovePosition), PublishPastMoves(nextState)))
            } else {
                assertEffects(setOf(PublishPastMoves(nextState)))
            }
        }

        verify {
            gameState.proceed(currentMovePosition)
            @Suppress("UnusedEquals")
            mockLightStrategy.equals(mockLightStrategy)
        }

        if (disk!!.isDark) {
            verify {
                mockLightStrategy.deriveNext(nextState)
                @Suppress("UnusedEquals")
                mockLightStrategy.equals(null)
            }
        }

        confirmVerified(gameState, mockLightStrategy)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if pass turn`(disk: Disk?) {
        val nextMovePosition = Position(1, 1)
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.flip()
            every { lastState } returns mockk()
        }
        val currentMovePosition = Position(2, 3)
        val gameState: CurrentGameState = mockk {
            every { proceed(any()) } returns PassTurn(nextState)
        }

        every { mockLightStrategy.deriveNext(nextState) } returns nextMovePosition

        val initModel = testModel.copy(
            gameState = gameState,
            nextMovePosition = currentMovePosition,
        )

        val expectedModel = initModel.resetNextTurn(nextState, passed = true)
        val expectedEffects = buildSet {
            if (disk!!.isDark && expectedModel.darkStrategy == HumanPlayer) {
                add(WaitBeforePassTurn(nextMovePosition, nextState))
            } else if (disk.isLight && expectedModel.lightStrategy != HumanPlayer) {
                add(WaitBeforePassTurn(null, nextState))
            }
            add(PublishPastMoves(nextState))
        }

        initModel after ContinueGame matches {
            assertModel(expectedModel)
            assertEffects(expectedEffects)
        }

        verify {
            gameState.proceed(currentMovePosition)
            @Suppress("UnusedEquals")
            mockLightStrategy.equals(mockLightStrategy)
        }

        if (disk!!.isDark) {
            verify {
                mockLightStrategy.deriveNext(nextState)
            }
        }

        if (disk.isLight) {
            verify {
                @Suppress("UnusedEquals")
                mockLightStrategy.equals(null)
            }
        }

        confirmVerified(gameState, mockLightStrategy)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if player wins`(disk: Disk?) {
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.flip()
        }
        val currentMovePosition = Position(2, 3)
        val gameState: CurrentGameState = mockk {
            every { proceed(any()) } returns Win(nextState, disk!!)
            every { currentDisk } returns disk
        }

        val initModel = testModel.copy(
            gameState = gameState,
            nextMovePosition = currentMovePosition,
        )

        val expectedModel = initModel.resetNextTurn(nextState)
        val expectedEffects = buildSet {
            add(WaitBeforeGameEnd(EndedWin(disk!!)))
            add(PublishPastMoves(newState = nextState, gameEnd = EndedWin(disk)))
        }

        initModel after ContinueGame matches {
            assertModel(expectedModel)
            assertEffects(expectedEffects)
        }

        verify {
            gameState.proceed(currentMovePosition)
        }

        confirmVerified(gameState)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if game is a tie`(disk: Disk?) {
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.flip()
        }
        val currentMovePosition = Position(2, 3)
        val gameState: CurrentGameState = mockk {
            every { proceed(any()) } returns Tie(nextState)
            every { currentDisk } returns disk!!
        }

        val initModel = testModel.copy(
            gameState = gameState,
            nextMovePosition = currentMovePosition,
        )

        val expectedModel = initModel.resetNextTurn(nextState)
        val expectedEffects = buildSet {
            add(WaitBeforeGameEnd(result = EndedTie))
            add(PublishPastMoves(newState = nextState, gameEnd = EndedTie))
        }

        initModel after ContinueGame matches {
            assertModel(expectedModel)
            assertEffects(expectedEffects)
        }

        verify {
            gameState.proceed(currentMovePosition)
        }

        confirmVerified(gameState)
    }

    @Test
    fun `OnTurnPassed works correctly`() {
        val nextPosition = (1 to 1)
        val newState: CurrentGameState = mockk()

        val initModel = testModel

        val expectedModel = initModel
            .resetNextTurn(newState)
            .pickNextMoveAt(nextPosition)

        initModel after OnTurnPassed(nextPosition, newState) matches {
            assertModel(expectedModel)
        }

        verify {
            @Suppress("UnusedEquals")
            newState.equals(newState)
        }

        confirmVerified(newState)
    }

    @Test
    @Parameters(method = "paramsForGameEnd")
    fun `OnGameEnded works correctly`(gameEnd: GameEnd) {
        val initModel = testModel

        val expectedModel = initModel.copy(
            ended = gameEnd,
        )

        initModel after OnGameEnded(gameEnd) matches {
            assertModel(expectedModel)
        }
    }

    @Suppress("unused")
    private fun paramsForGameEnd() = arrayOf(
        arrayOf(EndedWin(winner = Disk.Dark)),
        arrayOf(EndedWin(winner = Disk.Light)),
        arrayOf(EndedTie),
    )

    @Suppress("unused")
    private fun paramsForDisk() = arrayOf(
        arrayOf(Disk.Dark),
        arrayOf(Disk.Light),
    )
}
