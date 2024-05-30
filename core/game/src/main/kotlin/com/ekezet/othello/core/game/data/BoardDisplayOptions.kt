package com.ekezet.othello.core.game.data

data class BoardDisplayOptions(
    val showPossibleMoves: Boolean,
    val showBoardPositions: Boolean,
    val isGrayscaleMode: Boolean,
) {
    companion object
}
