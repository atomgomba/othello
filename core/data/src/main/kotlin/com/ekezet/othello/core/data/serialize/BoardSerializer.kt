package com.ekezet.othello.core.data.serialize

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.serialize.throwable.InvalidTokenException

object BoardSerializer {
    @Throws(
        InvalidTokenException::class,
        IllegalStateException::class,
    )
    fun fromLines(lines: List<String>): Board {
        val data = lines.map { it.trim() }
        var result = arrayOf<Array<Disk?>>()
        check(data.size == BoardHeight) { "BoardHeight must be $BoardHeight, but was ${data.size}" }
        for ((n, line) in data.withIndex()) {
            check(line.length == BoardWidth) { "Length of row ${n + 1} must be $BoardWidth, but was ${line.length}" }
            result += line.toRow()
        }
        return result
    }

    @Throws(
        InvalidTokenException::class,
        IllegalStateException::class,
    )
    fun fromLines(vararg lines: String) = fromLines(lines.toList())

    @Throws(
        InvalidTokenException::class,
        IllegalStateException::class,
    )
    fun fromString(lines: String) = fromLines(lines.split('\n'))

    fun toLines(board: Board): List<String> = board.map { row -> row.toLine() }

    @Throws(InvalidTokenException::class)
    private fun String.toRow(): Array<Disk?> = toCharArray().map { char ->
        when (char) {
            TOKEN_EMPTY -> null
            TOKEN_LIGHT -> Disk.Light
            TOKEN_DARK -> Disk.Dark
            else -> throw InvalidTokenException("Invalid token: '$char'")
        }
    }.toTypedArray()

    private fun Array<Disk?>.toLine(): String = map { disk ->
        when (disk) {
            Disk.Light -> TOKEN_LIGHT
            Disk.Dark -> TOKEN_DARK
            else -> TOKEN_EMPTY
        }
    }.joinToString("")
}
