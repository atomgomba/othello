package com.ekezet.othello.core.data.serialize

import com.ekezet.othello.core.data.models.Position
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PositionSerializerKtTest {
    @Test
    fun asString_NonNull() {
        assertEquals("A1", Position(0, 0).asString())
        assertEquals("H8", Position(7, 7).asString())
    }

    @Test
    fun asString_Null() {
        val position: Position? = null
        assertEquals("passed", position.asString())
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun asString_Error() {
        Position(8, 8).asString()
    }
}
