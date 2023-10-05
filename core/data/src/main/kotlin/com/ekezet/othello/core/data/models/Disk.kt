package com.ekezet.othello.core.data.models

@JvmInline
value class Disk private constructor(
    val isDark: Boolean,
) {
    companion object {
        val Light = Disk(false)
        val Dark = Disk(true)

        fun valueOf(value: String?): Disk? =
            when (value) {
                Light.toString() -> Light
                Dark.toString() -> Dark
                else -> null
            }
    }

    override fun toString() = if (isDark) "Dark" else "Light"
}

fun Disk.flip() = if (isDark) Disk.Light else Disk.Dark

val Disk.isLight: Boolean
    inline get() = !isDark
