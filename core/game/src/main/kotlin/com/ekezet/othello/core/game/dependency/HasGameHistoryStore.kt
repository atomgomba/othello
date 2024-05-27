package com.ekezet.othello.core.game.dependency

import com.ekezet.othello.core.game.store.GameHistoryStore

interface HasGameHistoryStore {
    val gameHistoryStore: GameHistoryStore
}
