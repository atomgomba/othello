package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.serialize.BoardSerializer
import com.ekezet.othello.core.game.throwable.InvalidMoveException
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class OthelloGameStateTest {
    @Test(expected = InvalidMoveException::class)
    fun testInvalidMove() {
        val subject = OthelloGameState.new()

        subject.proceed((7 to 7))
    }

    @Test(expected = InvalidMoveException::class)
    fun testNullMove() {
        val subject = OthelloGameState.new()

        subject.proceed(null)
    }

    @Test
    fun testTurn() {
        val subject = OthelloGameState(
            currentBoard = BoardFactory.starter(),
            history = listOf(mockk(), mockk()),
        )

        assertEquals(2, subject.turn)
    }

    @Test
    fun testCurrentDisk() {
        val subject = OthelloGameState(
            currentBoard = BoardFactory.starter(),
            history = listOf(mockk(), mockk()),
        )

        assertEquals(Disk.Dark, subject.currentDisk)
    }

    @Test
    fun testValidMoves() {
        val subject = OthelloGameState.new(BoardFactory.starter())

        val expected = setOf(
            ValidMove(
                position = (3 to 2),
                segment = ValidSegment(start = (3 to 2), end = (3 to 4), isStartValid = true)
            ),
            ValidMove(
                position = (4 to 5),
                segment = ValidSegment(start = (4 to 3), end = (4 to 5), isStartValid = false)
            ),
            ValidMove(
                position = (2 to 3),
                segment = ValidSegment(start = (2 to 3), end = (4 to 3), isStartValid = true)
            ),
            ValidMove(
                position = (5 to 4),
                segment = ValidSegment(start = (3 to 4), end = (5 to 4), isStartValid = false)
            ),
        )

        assertEquals(expected, subject.validMoves)
    }

    @Test
    fun testDiskCount() {
        val subject = OthelloGameState.new(BoardFactory.starter())

        assertEquals((2 to 2), subject.diskCount)
    }

    @Test
    fun testNextTurn() {
        val board = BoardSerializer.fromString(
            """
        --------
        --------
        --------
        ---ox---
        ---xo---
        --------
        --------
        --------
        """
        )

        val subject = OthelloGameState.new(board)

        val result = subject.proceed((4 to 5))

        assertIs<NextTurn>(result)
        assertEquals(1, result.state.turn)
        assertEquals(Disk.Light, result.state.currentDisk)
    }

    @Test
    fun testPassTurn() {
        val board = BoardSerializer.fromString(
            """
        xooooooo
        xoxxo---
        xoxxo---
        xoooo---
        xoooo---
        xoooo---
        xooxo---
        xooooo--
        """
        )

        val subject = OthelloGameState.new(board)

        val result = subject.proceed((6 to 7))

        assertIs<PassTurn>(result)
        assertEquals(Disk.Dark, result.state.currentDisk)
    }

    @Test
    fun testTie() {
        val board = BoardSerializer.fromString(
            """
        oooooooo
        oooooooo
        oooooooo
        oooooooo
        xxxxxxxx
        xxxxxxxx
        xxxxxxxx
        xoooooo-
        """
        )

        val subject = OthelloGameState.new(board)

        val result = subject.proceed((7 to 7))

        assertIs<Tie>(result)
        assertEquals(Disk.Light, result.state.currentDisk)
    }

    @Test
    fun testWin() {
        val board = BoardSerializer.fromString(
            """
        xooooooo
        oooooooo
        oooooooo
        oooooooo
        xxxxxxxx
        xxxxxxxx
        xxxxxxxx
        xoooooo-
        """
        )

        val subject = OthelloGameState.new(board)

        val result = subject.proceed((7 to 7))

        assertIs<Win>(result)
        assertEquals(Disk.Light, result.state.currentDisk)
    }

    @Test
    fun testEquals() {
        assertEquals(
            OthelloGameState.new(BoardFactory.starter()),
            OthelloGameState.new(BoardFactory.starter())
        )
    }

    @Test
    fun testHashcode() {
        assertEquals(
            OthelloGameState.new(BoardFactory.starter()).hashCode(),
            OthelloGameState.new(BoardFactory.starter()).hashCode()
        )
    }
}
