package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.getAt
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.serialize.BoardSerializer
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

internal class DiagonalMovesKtTest {
    @Test
    fun getLeftDiagonal() {
        val result = board.getLeftDiagonal(2, 1)
        val expected = arrayOf(null, Disk.Dark, Disk.Light, Disk.Dark, null, null)

        assertContentEquals(expected, result)
    }

    @Test
    fun transposeRight() {
        val input: Set<ValidSegment<Int>> = setOf(
            ValidSegment(0, 2, true),
            ValidSegment(2, 4, false),
        )
        val result = input.transposeRight(2, 1).toList().sortedBy { it.start.x }

        assertEquals(Disk.Light, board.getAt(result[0].invalidPosition))
        assertEquals(Disk.Light, board.getAt(result[1].invalidPosition))
    }

    @Test
    fun getRightDiagonal() {
        val result = board.getRightDiagonal(4, 4)
        val expected = arrayOf(null, Disk.Dark, Disk.Light, null)

        assertContentEquals(expected, result)
    }

    private val board: Board = BoardSerializer.fromLines(
        "--------",
        "--------",
        "---xx-o-",
        "----ox--",
        "-----xo-",
        "--------",
        "--------",
        "--------",
    )
}
