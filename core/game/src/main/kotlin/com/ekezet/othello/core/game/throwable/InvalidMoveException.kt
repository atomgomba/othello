package com.ekezet.othello.core.game.throwable

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.asString

data class InvalidMoveException(
    val disk: Disk,
    val invalidPosition: Position,
) : IllegalStateException() {
    override val message: String
        get() = "Invalid move attempt by $disk at ${invalidPosition.asString()}"
}
