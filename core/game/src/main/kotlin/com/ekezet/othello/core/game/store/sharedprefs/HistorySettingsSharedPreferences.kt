package com.ekezet.othello.core.game.store.sharedprefs

import android.content.SharedPreferences
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.HistoryDisplayOptions
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IHistorySettings

private const val KEY_ALWAYS_SCROLL_TO_BOTTOM = "alwaysScrollToBottom"

internal fun SharedPreferences.persist(data: IHistorySettings) = with(edit()) {
    putBoolean(KEY_ALWAYS_SCROLL_TO_BOTTOM, data.historyDisplayOptions.alwaysScrollToBottom)

    apply()
}

internal fun SharedPreferences.loadHistorySettings(): HistorySettings =
    HistorySettings(
        historyDisplayOptions = with(HistoryDisplayOptions.Default) {
            HistoryDisplayOptions(
                alwaysScrollToBottom = getBoolean(KEY_ALWAYS_SCROLL_TO_BOTTOM, alwaysScrollToBottom),
            )
        },
    )
