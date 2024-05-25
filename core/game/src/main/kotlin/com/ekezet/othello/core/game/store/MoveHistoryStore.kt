package com.ekezet.othello.core.game.store

import com.ekezet.othello.core.game.MoveHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

class MoveHistoryStore {
    private val _history = MutableStateFlow<MoveHistory>(emptyList())
    val history get() = _history.asStateFlow()

    fun reset(history: MoveHistory = emptyList()) {
        _history.value = history.toList()
        Timber.d("Move history has been reset")
    }
}
