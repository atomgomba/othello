package com.ekezet.othello.core.data.serialize

import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y

private val letters: String by lazy {
    ('A'..'Z').joinToString()
}

private val numbers: String by lazy {
    ('1'..'9').joinToString()
}

fun Position.serialize() = buildString {
    append(letters[x])
    append(numbers[y])
}
