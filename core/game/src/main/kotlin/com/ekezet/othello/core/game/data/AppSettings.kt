package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.data.ExcludeFromCoverage

interface IAppSettings {
    val confirmExit: Boolean
}

data class AppSettings(
    override val confirmExit: Boolean,
) : IAppSettings {
    companion object
}

@ExcludeFromCoverage
infix fun AppSettings.Companion.from(other: IAppSettings) = with(other) {
    AppSettings(
        confirmExit = confirmExit,
    )
}
