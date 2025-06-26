package com.ekezet.othello.core.game.strategy

import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.game.state.OthelloGameState

data class PreferSidesDecoratorStrategy(
    override val wrapped: Strategy,
) : DecoratedStrategy {
    override val name: String = "${wrapped.name} (Prefer sides)"

    private val sidesX = setOf(0, BoardWidth - 1)
    private val sidesY = setOf(0, BoardHeight - 1)

    override fun deriveNext(state: OthelloGameState): Position? = with(state) {
        val shuffledMoves = validMoves.shuffled()
        val corner = shuffledMoves.firstOrNull {
            (it.position.x in sidesX || it.position.y in sidesY) && it.position.x == it.position.y
        }?.position
        val side = shuffledMoves.firstOrNull {
            it.position.x in sidesX || it.position.y in sidesY
        }?.position
        corner ?: side ?: wrapped.deriveNext(state)
    }

    companion object {
        fun Strategy.preferSides() =
            this as? PreferSidesDecoratorStrategy ?: PreferSidesDecoratorStrategy(this)
    }
}
