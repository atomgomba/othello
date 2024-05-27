package com.ekezet.othello.core.ui.viewModels

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

typealias BoardList = ImmutableList<ImmutableList<Disk?>>

fun Board.toImmutableList(): BoardList = buildList {
    for (row in this@toImmutableList) {
        add(row.toList().toImmutableList())
    }
}.toImmutableList()

fun BoardList.getAt(x: Int, y: Int): Disk? =
    get(y)[x]
