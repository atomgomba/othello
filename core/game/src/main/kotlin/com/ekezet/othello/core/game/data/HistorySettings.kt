package com.ekezet.othello.core.game.data

import com.ekezet.othello.core.data.ExcludeFromCoverage

interface IHistorySettings {
    val historyDisplayOptions: HistoryDisplayOptions
    val showHistoryAsText: Boolean
}

data class HistorySettings(
    override val historyDisplayOptions: HistoryDisplayOptions,
    override val showHistoryAsText: Boolean,
) : IHistorySettings {
    companion object
}

@ExcludeFromCoverage
infix fun HistorySettings.Companion.from(other: IHistorySettings) = with(other) {
    HistorySettings(
        historyDisplayOptions = historyDisplayOptions,
        showHistoryAsText = showHistoryAsText,
    )
}
