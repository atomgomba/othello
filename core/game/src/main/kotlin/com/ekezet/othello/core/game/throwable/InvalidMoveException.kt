package com.ekezet.othello.core.game.throwable

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.serialize.asString

sealed class InvalidMoveException(message: String) : IllegalStateException(message)

data class InvalidNewMoveException(
    val disk: Disk,
    val invalidPosition: Position,
) : InvalidMoveException("Invalid move attempt by $disk at ${invalidPosition.asString()}")

class InvalidPastMoveException : InvalidMoveException("Past game state cannot be modified")
