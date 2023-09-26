package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.proceedAt

object BoardFactory {
    private fun empty(): Board = buildList<Array<Disk?>> {
        repeat(BoardHeight) {
            add(arrayOfNulls(BoardWidth))
        }
    }.toTypedArray()

    fun starter() = empty().apply {
        proceedAt(3, 3, Disk.Light)
        proceedAt(4, 4, Disk.Light)
        proceedAt(4, 3, Disk.Dark)
        proceedAt(3, 4, Disk.Dark)
    }
}
