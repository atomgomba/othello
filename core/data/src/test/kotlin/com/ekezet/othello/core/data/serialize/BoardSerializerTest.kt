package com.ekezet.othello.core.data.serialize

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.putAt
import kotlin.test.Test
import kotlin.test.assertEquals

internal class BoardSerializerTest {
    private val testBoard: Board = buildList<Array<Disk?>> {
        repeat(BoardHeight) {
            add(arrayOfNulls(BoardWidth))
        }
    }.toTypedArray().apply {
        putAt(3 to 3, Disk.Light)
        putAt(4 to 4, Disk.Light)
        putAt(4 to 3, Disk.Dark)
        putAt(3 to 4, Disk.Dark)
    }

    private val expected = """
        --------  
        --------  
        --------  
        ---ox---  
        ---xo---  
        --------  
        --------  
        --------       
    """.trimIndent().trim()

    @Test
    fun test() {
        val result = BoardSerializer.toString(testBoard)
        assertEquals(expected, result)
    }
}
