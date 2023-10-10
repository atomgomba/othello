package com.ekezet.othello.core.game.di

import android.content.Context
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.store.DefaultGameSettingsStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import org.koin.dsl.module

fun gameCoreModule() = module {
    single<GameSettingsStore> {
        val context: Context = get()
        val prefs = context.getSharedPreferences("game-settings", Context.MODE_PRIVATE)
        // TODO
        val settings = GameSettings()

        DefaultGameSettingsStore(initialGameSettings = settings)
    }
}
