package com.ekezet.othello.core.data.serialize

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y

val PositionLetters: List<Char> by lazy {
    ('A'..'H').toList()
}

val PositionNumbers: List<Char> by lazy {
    ('1'..'8').toList()
}

fun Position?.asString() = if (this == null) {
    "passed"
} else {
    "${PositionLetters[x]}${PositionNumbers[y]}"
}

fun String.toPosition() = if (length != 2) {
    null
} else {
    val (first, second) = toCharArray()
    val x = PositionLetters.indexOf(first)
    val y = second.digitToInt() + 1
    Position(x, y)
}
