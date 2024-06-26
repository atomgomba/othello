package com.ekezet.othello.feature.gameboard.ui.viewModels

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal typealias BoardOverlayList = ImmutableList<ImmutableList<OverlayItem?>>

internal fun BoardOverlay.toImmutableList(): BoardOverlayList = buildList {
    for (row in this@toImmutableList) {
        add(row.toList().toImmutableList())
    }
}.toImmutableList()

internal fun BoardOverlayList.getAt(x: Int, y: Int): OverlayItem? =
    get(y)[x]
