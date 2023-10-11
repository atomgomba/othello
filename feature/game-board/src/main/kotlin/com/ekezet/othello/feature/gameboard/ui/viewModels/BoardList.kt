package com.ekezet.othello.feature.gameboard.ui.viewModels

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal typealias BoardList = ImmutableList<ImmutableList<Disk?>>

internal fun Board.toList(): BoardList = buildList {
    for (row in this@toList) {
        add(row.toList().toImmutableList())
    }
}.toImmutableList()

internal fun BoardList.getAt(x: Int, y: Int): Disk? =
    get(y)[x]
