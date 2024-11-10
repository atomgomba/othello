package com.ekezet.othello.core.game.dependency

import com.ekezet.othello.core.game.store.HistorySettingsStore

interface HasHistorySettingsStore {
    val historySettingsStore: HistorySettingsStore
}
