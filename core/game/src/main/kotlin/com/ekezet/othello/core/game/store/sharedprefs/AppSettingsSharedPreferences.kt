package com.ekezet.othello.core.game.store.sharedprefs

import android.content.SharedPreferences
import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.IAppSettings

private const val KEY_CONFIRM_EXIT = "confirmExit"

internal fun SharedPreferences.persist(data: IAppSettings) = with(edit()) {
    putBoolean(KEY_CONFIRM_EXIT, data.confirmExit)

    apply()
}

internal fun SharedPreferences.loadAppSettings(): AppSettings = with(AppSettings.Default) {
    AppSettings(
        confirmExit = getBoolean(KEY_CONFIRM_EXIT, confirmExit),
    )
}
