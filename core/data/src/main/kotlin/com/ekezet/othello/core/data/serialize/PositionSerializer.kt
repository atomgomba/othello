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
