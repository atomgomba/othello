package com.ekezet.othello.core.data.serialize

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y

private val letters: List<Char> by lazy {
    ('A'..'Z').toList()
}

private val numbers: List<Char> by lazy {
    ('1'..'9').toList()
}

fun Position.serialize() = buildString {
    append(letters[x])
    append(numbers[y])
}
