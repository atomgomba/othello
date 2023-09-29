package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.putAt

object BoardFactory {
    private fun empty(): Board = buildList<Array<Disk?>> {
        repeat(BoardHeight) {
            add(arrayOfNulls(BoardWidth))
        }
    }.toTypedArray()

    fun starter() = empty().apply {
        putAt(3 to 3, Disk.Light)
        putAt(4 to 4, Disk.Light)
        putAt(4 to 3, Disk.Dark)
        putAt(3 to 4, Disk.Dark)
    }
}
