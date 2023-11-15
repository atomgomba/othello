package com.ekezet.othello.main.navigation

internal fun String.stripRoute(): String =
    split("?").firstOrNull() ?: this
