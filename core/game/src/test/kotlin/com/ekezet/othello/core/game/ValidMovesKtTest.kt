package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Disk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ValidMovesKtTest {
    private val subject = Disk.Dark
    private val other = !subject

    @Test
    fun `findValidIndices should return something`() {
        val input1 = arrayOf(subject, other, null)
        val result1 = findValidIndices(input1, subject)
        assertEquals(setOf(ValidSegment(0, 2, false)), result1, "Unexpected result1")

        val input2 = arrayOf(null, subject, other, null)
        val result2 = findValidIndices(input2, subject)
        assertEquals(setOf(ValidSegment(1, 3, false)), result2, "Unexpected result2")

        val input3 = arrayOf(subject, other, other, null, null, other, subject)
        val result3 = findValidIndices(input3, subject)
        assertEquals(setOf(ValidSegment(0, 3, false), ValidSegment(4, 6, true)), result3, "Unexpected result3")

        val input4 = arrayOf(subject, other, other, null, null)
        val result4 = findValidIndices(input4, subject)
        assertEquals(setOf(ValidSegment(0, 3, false)), result4, "Unexpected result4")

        val input5 = arrayOf(null, null, subject, other, other, null)
        val result5 = findValidIndices(input5, subject)
        assertEquals(setOf(ValidSegment(2, 5, false)), result5, "Unexpected result5")
    }

    @Test
    fun `findValidIndices should return nothing`() {
        val input1 = arrayOf(other, other, other, subject)
        val result1 = findValidIndices(input1, subject)
        assertTrue(result1.isEmpty(), "Unexpected result1")

        val input2: Array<Disk?> = arrayOf(null, null, null, null)
        val result2 = findValidIndices(input2, subject)
        assertTrue(result2.isEmpty(), "Unexpected result2")

        val input3 = arrayOf(subject, other, other, subject, null)
        val result3 = findValidIndices(input3, subject)
        assertTrue(result3.isEmpty(), "Unexpected result3")

        val input4 = arrayOf(other, other, other, null, null)
        val result4 = findValidIndices(input4, subject)
        assertTrue(result4.isEmpty(), "Unexpected result4")
    }

    @Test
    fun `parts should return correct result`() {
        val input1 = ValidSegment(0 to 0, 2 to 0, false)
        val result1 = input1.parts()
        val expected1 = setOf(1 to 0)
        assertEquals(expected1, result1, "Unexpected result1")

        val input2 = ValidSegment(2 to 0, 0 to 0, false)
        val result2 = input2.parts()
        val expected2 = setOf(1 to 0)
        assertEquals(expected2, result2, "Unexpected result2")

        val input3 = ValidSegment(0 to 0, 0 to 2, false)
        val result3 = input3.parts()
        val expected3 = setOf(0 to 1)
        assertEquals(expected3, result3, "Unexpected result3")

        val input4 = ValidSegment(0 to 2, 0 to 0, false)
        val result4 = input4.parts()
        val expected4 = setOf(0 to 1)
        assertEquals(expected4, result4, "Unexpected result4")

        val input5 = ValidSegment(0 to 0, 3 to 3, false)
        val result5 = input5.parts()
        val expected5 = setOf(1 to 1, 2 to 2)
        assertEquals(expected5, result5, "Unexpected result5")

        val input6 = ValidSegment(3 to 3, 0 to 0, false)
        val result6 = input6.parts()
        val expected6 = setOf(1 to 1, 2 to 2)
        assertEquals(expected6, result6, "Unexpected result6")

        val input7 = ValidSegment(2 to 6, 4 to 4, false)
        val result7 = input7.parts()
        val expected7 = setOf(3 to 5)
        assertEquals(expected7, result7, "Unexpected result7")
    }
}
