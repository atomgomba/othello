package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.game.GameState
import com.ekezet.othello.core.game.MoveHistory

class ReplayStrategy(history: MoveHistory, disk: Disk) : Strategy {
    override val name: String
        get() = "Replay"

    private val moves = history.filter { it.disk == disk }
    private var index: Int = 0

    override fun deriveNext(state: GameState): Position? {
        val pastMove = moves.getOrNull(index) ?: return null
        index += 1
        return pastMove.moveAt
    }
}