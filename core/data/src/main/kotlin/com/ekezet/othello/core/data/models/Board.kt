package com.ekezet.othello.core.data.models

typealias Board = Array<Array<Disk?>>

const val BoardWidth = 8
const val BoardHeight = 8

fun Board.putAtAndClone(x: Int, y: Int, disk: Disk): Board =
    deepClone().apply {
        this[y] = this[y].apply {
            this[x] = disk
        }
    }

fun Board.putAtAndClone(position: Position, disk: Disk): Board =
    putAtAndClone(position.x, position.y, disk)

fun Board.putAt(position: Position, disk: Disk) {
    this[position.y][position.x] = disk
}

fun Board.getAt(position: Position): Disk? =
    this[position.y][position.x]

fun Board.deepClone() = clone().map { it.clone() }.toTypedArray()

/**
 * dark, light
 */
val Board.diskCount: DiskCount
    get() =
        flatten()
            .filterNotNull()
            .fold(DiskCount(0, 0)) { acc, disk ->
                DiskCount(
                    first = if (disk.isDark) acc.first + 1 else acc.first,
                    second = if (disk.isLight) acc.second + 1 else acc.second,
                )
            }

typealias DiskCount = Pair<Int, Int>

val DiskCount.numDark: Int
    inline get() = first

val DiskCount.numLight: Int
    inline get() = second
