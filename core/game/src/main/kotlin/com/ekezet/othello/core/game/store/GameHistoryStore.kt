package com.ekezet.othello.core.game.store

import com.ekezet.othello.core.game.GameEnd
import com.ekezet.othello.core.game.GameHistory
import com.ekezet.othello.core.game.MoveHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class GameHistoryStore {
    private val _history = MutableStateFlow(GameHistory(emptyList(), null))
    val history get() = _history.asStateFlow()

    fun reset(history: MoveHistory = emptyList(), gameEnd: GameEnd? = null) {
        _history.value = GameHistory(history.toList(), gameEnd)
        Timber.d("Move history has been reset")
    }
}
