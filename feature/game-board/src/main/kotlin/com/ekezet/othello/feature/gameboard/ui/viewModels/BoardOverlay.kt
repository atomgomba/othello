package com.ekezet.othello.feature.gameboard.ui.viewModels

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.feature.gameboard.ui.viewModels.OverlayItem.StackedOverlayItem
import java.util.Stack

internal typealias BoardOverlay = Array<Array<OverlayItem?>>

internal fun BoardOverlay.putAt(x: Int, y: Int, item: OverlayItem?): BoardOverlay = apply {
    val newItem = if (item != null && this[y][x] != null) {
        if (this[y][x] is StackedOverlayItem) {
            (this[y][x] as StackedOverlayItem).push(item)
        } else {
            StackedOverlayItem(
                Stack<OverlayItem>().apply {
                    push(this@putAt[y][x])
                    push(item)
                },
            )
        }
    } else {
        item
    }
    this[y][x] = newItem
}

internal fun BoardOverlay.putAt(position: Position, item: OverlayItem?): BoardOverlay {
    val (x, y) = position
    return putAt(x, y, item)
}

internal fun Board.newEmptyOverlay(): BoardOverlay = buildList<Array<OverlayItem?>> {
    repeat(this@newEmptyOverlay.size) {
        add(arrayOfNulls(this@newEmptyOverlay[0].size))
    }
}.toTypedArray()
