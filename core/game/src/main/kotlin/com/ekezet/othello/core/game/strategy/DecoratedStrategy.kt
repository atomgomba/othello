package com.ekezet.othello.core.game.strategy

interface DecoratedStrategy : Strategy {
    val wrapped: Strategy
}
