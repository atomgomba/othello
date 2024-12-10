package com.ekezet.othello.feature.gameboard.actions

import com.ekezet.hurok.test.after
import com.ekezet.hurok.test.matches
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameEnd.EndedTie
import com.ekezet.othello.core.game.GameEnd.EndedWin
import com.ekezet.othello.core.game.NextTurn
import com.ekezet.othello.core.game.PassTurn
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.game.Tie
import com.ekezet.othello.core.game.ValidMove
import com.ekezet.othello.core.game.Win
import com.ekezet.othello.core.game.data.StartBoard
import com.ekezet.othello.core.game.state.CurrentGameState
import com.ekezet.othello.core.game.state.OthelloGameState
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
    private val mockGameState: OthelloGameState = mockk()
    private val mockLightStrategy: Strategy = mockk()

    private val testModel = GameBoardModel(
        gameState = mockGameState,
        lightStrategy = mockLightStrategy,
    )

    @Test
    fun `OnGameStarted works correctly if Dark is HumanPlayer`() {
        every { mockGameState.currentDisk } returns Disk.Dark

        val initModel = testModel

        initModel after OnLoopStarted matches {
            assertSkipped()
        }
    }

    @Test
    @Parameters(method = "paramsForGameEnd")
    fun `OnGameStarted works correctly if game has ended`(ended: GameEnd?) {
        val initModel = testModel.copy(
            ended = ended!!,
        )

        initModel after OnLoopStarted matches {
            assertSkipped()
        }
    }

    @Test
    fun `OnGameStarted works correctly if Dark is NPC`() {
        val nextMove = Position(42, 42)
        val darkStrategy: Strategy = mockk {
            every { deriveNext(mockGameState) } returns nextMove
        }
        every { mockGameState.turn } returns 0
        every { mockGameState.currentDisk } returns Disk.Dark

        val initModel = testModel.copy(
            darkStrategy = darkStrategy,
            gameState = mockGameState,
        )

        initModel after OnLoopStarted matches {
            assertEffects(setOf(WaitBeforeNextTurn(nextMove)))
        }

        verify {
            darkStrategy.deriveNext(mockGameState)
            darkStrategy.equals(HumanPlayer)
            mockGameState.turn
            mockGameState.currentDisk
        }

        confirmVerified(mockGameState, darkStrategy)
    }

    @Test
    fun `OnGameStarted works correctly if Dark is NPC and cannot move`() {
        val darkStrategy: Strategy = mockk {
            every { deriveNext(mockGameState) } returns null
        }
        every { mockGameState.turn } returns 0
        every { mockGameState.currentDisk } returns Disk.Dark

        val initModel = testModel.copy(
            darkStrategy = darkStrategy,
            gameState = mockGameState,
        )

        assertFailsWith(IllegalStateException::class) {
            initModel after OnLoopStarted
        }

        verify {
            darkStrategy.deriveNext(mockGameState)
            darkStrategy.equals(null)
            testModel.lightStrategy == null
            mockGameState.turn
            mockGameState.currentDisk
        }

        confirmVerified(mockGameState, darkStrategy)
    }

    @Test
    fun `OnMoveMade works correctly if move is valid`() {
        val position = Position(1, 2)
        every { mockGameState.validMoves } returns setOf(ValidMove(position, mockk()))
        every { mockGameState.turn } returns 0

        val initModel = testModel.copy(
            gameState = mockGameState,
        )

        val expectedModel = initModel.pickNextMoveAt(position)

        initModel after OnMoveMade(position) matches {
            assertModel(expectedModel)
            assertNoEffects()
        }

        verify {
            mockGameState.validMoves
            mockGameState.turn
            @Suppress("UnusedEquals")
            mockGameState.equals(mockGameState)
        }

        confirmVerified(mockGameState)
    }

    @Test
    fun `OnMoveMade works correctly if move is invalid`() {
        val position = Position(42, 42)
        every { mockGameState.turn } returns 0
        every { mockGameState.validMoves } returns emptySet()

        val initModel = testModel

        initModel after OnMoveMade(position) matches {
            assertSkipped()
        }

        verify {
            mockGameState.turn
            mockGameState.validMoves
        }

        confirmVerified(mockGameState)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if move is invalid`(disk: Disk?) {
        val nextMovePosition = Position(2, 3)
        every { mockGameState.proceed(any()) } throws InvalidNewMoveException(disk!!, nextMovePosition)
        every { mockGameState.currentDisk } returns disk
        every { mockGameState.turn } returns 1
        val pastMove = mockk<PastMove> {
            every { board } returns StartBoard
        }
        every { mockGameState.pastMoves } returns listOf(pastMove)

        val initModel = testModel.copy(
            gameState = mockGameState,
            nextMovePosition = nextMovePosition,
        )

        initModel after ContinueGame matches {
            assertSkipped()
        }

        verify {
            mockGameState.turn
            mockGameState.pastMoves
        }

        confirmVerified(mockGameState)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if next turn`(disk: Disk?) {
        val nextMovePosition = Position(1, 1)
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.not()
        }
        val currentMovePosition = Position(2, 3)
        every { mockGameState.proceed(any()) } returns NextTurn(nextState)
        every { mockGameState.currentDisk } returns disk!!
        every { mockGameState.turn } returns 0

        every { mockLightStrategy.deriveNext(nextState) } returns nextMovePosition

        val initModel = testModel.copy(
            gameState = mockGameState,
            nextMovePosition = currentMovePosition,
        )

        val expectedModel = initModel.resetNextTurn(nextState)

        initModel after ContinueGame matches {
            assertModel(expectedModel)

            if (disk.isDark && expectedModel.darkStrategy == HumanPlayer) {
                assertEffects(setOf(WaitBeforeNextTurn(nextMovePosition), PublishPastMoves(nextState)))
            } else {
                assertEffects(setOf(PublishPastMoves(nextState)))
            }
        }

        verify {
            mockGameState.turn
            mockGameState.proceed(currentMovePosition)
            @Suppress("UnusedEquals")
            mockLightStrategy.equals(mockLightStrategy)
        }

        if (disk.isDark) {
            verify {
                mockLightStrategy.deriveNext(nextState)
                @Suppress("UnusedEquals")
                mockLightStrategy.equals(null)
            }
        }

        confirmVerified(mockGameState, mockLightStrategy)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if pass turn`(disk: Disk?) {
        val nextMovePosition = Position(1, 1)
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.not()
            every { lastState } returns mockk()
        }
        val currentMovePosition = Position(2, 3)
        every { mockGameState.proceed(any()) } returns PassTurn(nextState)
        every { mockGameState.turn } returns 0
        val pastMove = mockk<PastMove> {
            every { board } returns StartBoard
        }
        every { mockGameState.pastMoves } returns listOf(pastMove)

        every { mockLightStrategy.deriveNext(nextState) } returns nextMovePosition

        val initModel = testModel.copy(
            gameState = mockGameState,
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
            mockGameState.turn
            mockGameState.proceed(currentMovePosition)
            mockLightStrategy == mockLightStrategy
            nextState.lastState
            nextState.currentDisk
            nextState == nextState
            nextState.hashCode()
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

        confirmVerified(mockGameState, mockLightStrategy, nextState, pastMove)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if player wins`(disk: Disk?) {
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.not()
        }
        val currentMovePosition = Position(2, 3)
        every { mockGameState.proceed(any()) } returns Win(nextState, disk!!)
        every { mockGameState.currentDisk } returns disk
        every { mockGameState.turn } returns 0
        val pastMove = mockk<PastMove> {
            every { board } returns StartBoard
        }
        every { mockGameState.pastMoves } returns listOf(pastMove)

        val initModel = testModel.copy(
            gameState = mockGameState,
            nextMovePosition = currentMovePosition,
        )

        val expectedModel = initModel.resetNextTurn(nextState)
        val expectedEffects = buildSet {
            add(WaitBeforeGameEnd(EndedWin(disk)))
            add(PublishPastMoves(newState = nextState, gameEnd = EndedWin(disk)))
        }

        initModel after ContinueGame matches {
            assertModel(expectedModel)
            assertEffects(expectedEffects)
        }

        verify {
            mockGameState.turn
            mockGameState.proceed(currentMovePosition)
            nextState == nextState
            nextState.hashCode()
            mockLightStrategy == mockLightStrategy
        }

        confirmVerified(mockGameState, nextState, mockLightStrategy, pastMove)
    }

    @Test
    @Parameters(method = "paramsForDisk")
    fun `ContinueGame works correctly if game is a tie`(disk: Disk?) {
        val nextState: CurrentGameState = mockk {
            every { currentDisk } returns disk!!.not()
        }
        val currentMovePosition = Position(2, 3)
        every { mockGameState.proceed(any()) } returns Tie(nextState)
        every { mockGameState.currentDisk } returns disk!!
        every { mockGameState.turn } returns 0
        every { mockGameState.pastMoves } returns emptyList()

        val initModel = testModel.copy(
            gameState = mockGameState,
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
            mockGameState.turn
            mockGameState.proceed(currentMovePosition)
            nextState == nextState
            nextState.hashCode()
            mockLightStrategy == mockLightStrategy
        }

        confirmVerified(mockGameState, nextState, mockLightStrategy)
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

    @Test
    fun `OnNextTurnClicked works correctly if has next turn`() {
        every { mockGameState.turn } returns 1

        val initModel = testModel

        val expectedModel = initModel.copy(
            selectedTurn = initModel.selectedTurn + 1,
        )

        initModel after OnNextTurnClicked matches {
            assertModel(expectedModel)
        }
    }

    @Test
    fun `OnNextTurnClicked works correctly if last turn`() {
        every { mockGameState.turn } returns 1

        val initModel = testModel.copy(
            selectedTurn = 1,
        )

        initModel after OnNextTurnClicked matches {
            assertModel(initModel)
        }
    }

    @Test
    fun `OnPreviousTurnClicked works correctly if has previous turn`() {
        val initModel = testModel.copy(
            selectedTurn = 1,
        )

        val expectedModel = initModel.copy(
            selectedTurn = initModel.selectedTurn - 1,
        )

        initModel after OnPreviousTurnClicked matches {
            assertModel(expectedModel)
        }
    }

    @Test
    fun `OnPreviousTurnClicked works correctly if first turn`() {
        val initModel = testModel

        initModel after OnPreviousTurnClicked matches {
            assertModel(initModel)
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
