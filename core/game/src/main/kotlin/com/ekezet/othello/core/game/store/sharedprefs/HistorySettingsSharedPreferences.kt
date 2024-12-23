package com.ekezet.othello.core.game.store.sharedprefs

import android.content.SharedPreferences
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.HistoryDisplayOptions
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IHistorySettings

private const val KEY_ALWAYS_SCROLL_TO_BOTTOM = "alwaysScrollToBottom"
private const val KEY_SHOW_HISTORY_AS_TEXT = "showHistoryAsText"

internal fun SharedPreferences.persist(data: IHistorySettings) = with(edit()) {
    putBoolean(KEY_ALWAYS_SCROLL_TO_BOTTOM, data.historyDisplayOptions.alwaysScrollToBottom)
    putBoolean(KEY_SHOW_HISTORY_AS_TEXT, data.showHistoryAsText)

    apply()
}

internal fun SharedPreferences.loadHistorySettings(): HistorySettings = with(HistorySettings.Default) {
    HistorySettings(
        historyDisplayOptions = with(historyDisplayOptions) {
            HistoryDisplayOptions(
                alwaysScrollToBottom = getBoolean(KEY_ALWAYS_SCROLL_TO_BOTTOM, alwaysScrollToBottom),
            )
        },
        showHistoryAsText = showHistoryAsText,
    )
}
