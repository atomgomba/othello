package com.ekezet.othello.core.data.models

import com.ekezet.othello.core.data.models.Disk.Dark
import com.ekezet.othello.core.data.models.Disk.Light

sealed interface Disk {
    data object Dark : Disk
    data object Light : Disk

    companion object
}

fun Disk.flip() = if (isDark) Light else Dark

val Disk.isDark: Boolean
    inline get() = this is Dark

val Disk.isLight: Boolean
    inline get() = this is Light

fun Disk.Companion.valueOf(value: String?): Disk? = when (value) {
    "$Dark" -> Dark
    "$Light" -> Light
    else -> null
}
