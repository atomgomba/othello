package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.putAndCloneAt

object BoardFactory {
    private fun empty(): Board = buildList<Array<Disk?>> {
        repeat(BoardHeight) {
            add(arrayOfNulls(BoardWidth))
        }
    }.toTypedArray()

    fun starter() = empty().apply {
        putAndCloneAt(3, 3, Disk.Light)
        putAndCloneAt(4, 4, Disk.Light)
        putAndCloneAt(4, 3, Disk.Dark)
        putAndCloneAt(3, 4, Disk.Dark)
    }
}
