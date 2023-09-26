package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.absoluteValue
import com.ekezet.othello.core.data.models.flip
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import kotlin.math.absoluteValue
import kotlin.math.sign

data class ValidSegment<out T : Any>(
    val start: T,
    val end: T,
    val isStartValid: Boolean,
)

internal val <T : Any> ValidSegment<T>.validPosition: T
    get() = if (isStartValid) start else end

internal val <T : Any> ValidSegment<T>.invalidPosition: T
    get() = if (isStartValid) end else start

data class ValidMove(
    val position: Position,
    val segment: ValidSegment<Position>,
)

internal fun Board.findValidMoves(subject: Disk): Set<ValidMove> {
    val result = mutableSetOf<ValidSegment<Position>>()
    val maxY = BoardHeight - 1
    for (y in 0 until BoardHeight) {
        for (x in 0 until BoardWidth) {
            val column = map { it[x] }.toTypedArray()
            val vertical = findValidIndices(column, subject)
            result.addAll(vertical.mapToX(x))

            val row = get(y)
            val horizontal = findValidIndices(row, subject)
            result.addAll(horizontal.mapToY(y))

            if (y == 0 || x == 0) {
                val leftDiagonal = getLeftDiagonal(x, y)
                val left = findValidIndices(leftDiagonal, subject)
                result.addAll(left.transposeRight(x, y))
            }

            if (y == maxY || x == 0) {
                val rightDiagonal = getRightDiagonal(x, y)
                val right = findValidIndices(rightDiagonal, subject)
                val transposed = right.transposeLeft(x, y)
                result.addAll(transposed)
            }
        }
    }
    return result.map {
        ValidMove(
            position = it.validPosition,
            segment = it,
        )
    }.toSet()
}

fun Set<ValidMove>.isValid(position: Position): Boolean =
    any { it.position == position }

fun Set<ValidMove>.isInvalid(position: Position) =
    !isValid(position)

internal fun findValidIndices(disks: Array<out Disk?>, subject: Disk): Set<ValidSegment<Int>> {
    if (disks.toSet().size != 3) {
        // for a possible valid move, data must contain both one of each kind of disks and an empty slot
        return emptySet()
    }

    val other = subject.flip()
    val result = mutableSetOf<ValidSegment<Int>>()
    var startIndex: Int? = null

    for ((i, disk) in disks.withIndex()) {
        if (0 < i && disk == other && startIndex == null) {
            startIndex = i - 1
            continue
        }
        if (startIndex != null && disk != other) {
            if (disks[startIndex] == subject && disks[i] == null) {
                result.add(ValidSegment(start = startIndex, end = i, isStartValid = false))
            } else if (disks[startIndex] == null && disks[i] == subject) {
                result.add(ValidSegment(start = startIndex, end = i, isStartValid = true))
            }
            startIndex = null
        }
    }

    return result
}

internal fun Set<ValidSegment<Int>>.mapToY(y: Int): Set<ValidSegment<Position>> = map {
    ValidSegment(
        start = it.start to y,
        end = it.end to y,
        isStartValid = it.isStartValid,
    )
}.toSet()

internal fun Set<ValidSegment<Int>>.mapToX(x: Int): Set<ValidSegment<Position>> = map {
    ValidSegment(
        start = x to it.start,
        end = x to it.end,
        isStartValid = it.isStartValid,
    )
}.toSet()

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

internal fun ValidSegment<Position>.parts(): Set<Position> {
    val result = mutableListOf<Position>()
    if (start.y == end.y) {
        val range = if (start.x < end.x) {
            start.x..end.x
        } else {
            start.x downTo end.x
        }
        for (x in range) {
            result.add(Position(x, start.y))
        }
    } else if (start.x == end.x) {
        val range = if (start.y < end.y) {
            start.y..end.y
        } else {
            start.y downTo end.y
        }
        for (y in range) {
            result.add(Position(start.x, y))
        }
    } else {
        val distance = end.y - start.y
        val dirY = distance.sign
        val dirX = (end.x - start.x).sign
        for (n in 0..distance.absoluteValue) {
            result.add(Position(start.x + n * dirX, start.y + n * dirY).absoluteValue)
        }
    }
    if (1 < result.size) {
        result.removeFirst()
        result.removeLast()
    }
    return result.toSet()
}
