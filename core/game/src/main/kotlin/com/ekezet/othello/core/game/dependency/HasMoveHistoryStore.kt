package com.ekezet.othello.core.game.dependency

import com.ekezet.othello.core.game.store.MoveHistoryStore

interface HasMoveHistoryStore {
    val moveHistoryStore: MoveHistoryStore
}
