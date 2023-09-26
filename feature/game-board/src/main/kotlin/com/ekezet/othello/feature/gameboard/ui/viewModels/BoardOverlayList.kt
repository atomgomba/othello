package com.ekezet.othello.feature.gameboard.ui.viewModels

typealias BoardOverlayList = List<List<OverlayItem?>>

internal fun BoardOverlay.toList(): BoardOverlayList = buildList {
    for (row in this@toList) {
        add(row.toList())
    }
}.toList()

internal fun BoardOverlayList.getAt(x: Int, y: Int): OverlayItem? =
    get(y)[x]
