package com.ekezet.othello.feature.gameboard.ui.viewModels

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk

internal typealias BoardList = List<List<Disk?>>

internal fun Board.toList(): BoardList = buildList {
    for (row in this@toList) {
        add(row.toList())
    }
}.toList()

internal fun BoardList.getAt(x: Int, y: Int): Disk? =
    get(y)[x]
