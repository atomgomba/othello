package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position

internal fun Board.getLeftDiagonal(fromX: Int, fromY: Int) =
    getDiagonal(fromX, fromY until BoardHeight)

internal fun Board.getRightDiagonal(fromX: Int, fromY: Int) =
    getDiagonal(fromX, fromY downTo 0)

private fun Board.getDiagonal(fromX: Int, rangeY: IntProgression): Array<Disk?> {
    var result = arrayOf<Disk?>()
    for ((offset, y) in rangeY.withIndex()) {
        val left = fromX + offset
        if (BoardWidth == left) {
            break
        }
        val disk: Disk? = get(y)[left]
        result += disk
    }
    return result
}

internal fun Set<ValidSegment<Int>>.transposeRight(x: Int, y: Int): Set<ValidSegment<Position>> =
    map {
        ValidSegment(
            start = Position(x + it.start, y + it.start),
            end = Position(x + it.end, y + it.end),
            isStartValid = it.isStartValid,
        )
    }.toSet()

internal fun Set<ValidSegment<Int>>.transposeLeft(x: Int, y: Int): Set<ValidSegment<Position>> =
    map {
        ValidSegment(
            start = Position(x + it.start, y - it.start),
            end = Position(x + it.end, y - it.end),
            isStartValid = it.isStartValid,
        )
    }.toSet()
