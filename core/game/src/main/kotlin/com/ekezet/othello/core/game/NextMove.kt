package com.ekezet.othello.core.game

import com.ekezet.othello.core.data.models.Board
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import java.util.Stack

data class NextMove(val position: Position, val disk: Disk)

data class PastMove(val source: Board, val move: NextMove?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PastMove

        if (!source.contentDeepEquals(other.source)) return false
        if (move != other.move) return false

        return true
    }

    override fun hashCode(): Int {
        var result = source.contentDeepHashCode()
        result = 31 * result + (move?.hashCode() ?: 0)
        return result
    }
}

typealias MoveHistory = Stack<PastMove>
