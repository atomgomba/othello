package com.ekezet.othello.core.data.models

@JvmInline
value class Disk private constructor(
    val isDark: Boolean,
) {
    companion object {
        @JvmStatic
        val Light = Disk(false)

        @JvmStatic
        val Dark = Disk(true)
    }

    override fun toString() = if (isDark) "Dark" else "Light"
}

fun Disk.flip() = if (isDark) Disk.Light else Disk.Dark

val Disk.isLight: Boolean
    inline get() = !isDark

fun Disk.Companion.valueOf(value: String?): Disk? = when (value) {
    "$Dark" -> Dark
    "$Light" -> Light
    else -> null
}

fun Disk.Companion.valueOf(value: Boolean?): Disk? = when (value) {
    false -> Dark
    true -> Light
    null -> null
}
