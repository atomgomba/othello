package com.ekezet.othello.main.di

import android.content.Context
import com.ekezet.othello.core.game.di.GameCoreBindings
import com.ekezet.othello.core.game.store.AppSettingsStore
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.ui.di.UiCoreBindings
import com.ekezet.othello.core.ui.render.MovesRenderer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides

@DependencyGraph(
    scope = AppScope::class,
    bindingContainers = [GameCoreBindings::class, UiCoreBindings::class],
)
interface MainGraph {
    val context: Context
    val gameSettingsStore: GameSettingsStore
    val historySettingsStore: HistorySettingsStore
    val appSettingsStore: AppSettingsStore
    val gameHistoryStore: GameHistoryStore
    val movesRenderer: MovesRenderer

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): MainGraph
    }
}
