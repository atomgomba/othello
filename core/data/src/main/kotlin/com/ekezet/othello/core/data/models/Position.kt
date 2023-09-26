package com.ekezet.othello.core.data.models

import kotlin.math.absoluteValue

typealias Position = Pair<Int, Int>

val Position.x: Int
    inline get() = first

val Position.y: Int
    inline get() = second

val Position.absoluteValue: Position
    inline get() = Position(x.absoluteValue, y.absoluteValue)
