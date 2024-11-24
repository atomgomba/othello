package com.ekezet.othello.core.game.dependency

import com.ekezet.othello.core.game.store.AppSettingsStore

interface HasAppSettingsStore {
    val appSettingsStore: AppSettingsStore
}
