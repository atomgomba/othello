package com.ekezet.othello.core.data.models

@JvmInline
value class Disk private constructor(
    val isDark: Boolean,
) {
    companion object {
        val Light = Disk(false)
        val Dark = Disk(true)
    }
}

fun Disk.flip() = if (isDark) Disk.Light else Disk.Dark

val Disk.isLight: Boolean
    inline get() = !isDark
