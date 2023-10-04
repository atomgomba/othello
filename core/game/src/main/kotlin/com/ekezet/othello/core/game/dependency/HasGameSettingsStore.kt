package com.ekezet.othello.core.game.dependency

import com.ekezet.othello.core.game.store.GameSettingsStore

interface HasGameSettingsStore {
    val gameSettingsStore: GameSettingsStore
}
