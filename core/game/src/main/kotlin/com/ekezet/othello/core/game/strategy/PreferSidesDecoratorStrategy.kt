package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.game.OthelloGameState

class PreferSidesDecoratorStrategy(
    override val wrapped: Strategy,
) : DecoratedStrategy {
    override val name: String
        get() = "${wrapped.name} (Prefer sides)"

    private val sidesX = setOf(0, BoardWidth - 1)
    private val sidesY = setOf(0, BoardHeight - 1)

    override fun deriveNext(state: OthelloGameState): Position? = with(state) {
        validMoves.shuffled().firstOrNull { it.position.x in sidesX || it.position.y in sidesY }?.position
            ?: wrapped.deriveNext(state)
    }

    companion object {
        fun Strategy.preferSides() = if (this is PreferSidesDecoratorStrategy) {
            this
        } else {
            PreferSidesDecoratorStrategy(this)
        }
    }
}
