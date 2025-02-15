package com.ekezet.othello.core.game.di

import com.ekezet.othello.core.game.store.AppSettingsStore
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.game.store.SharedPreferencesAppSettingsStore
import com.ekezet.othello.core.game.store.SharedPreferencesGameSettingsStore
import com.ekezet.othello.core.game.store.SharedPreferencesHistorySettingsStore
import org.koin.dsl.module

fun gameCoreModule() = module {
    single<GameSettingsStore> {
        SharedPreferencesGameSettingsStore(context = get())
    }

    single<HistorySettingsStore> {
        SharedPreferencesHistorySettingsStore(context = get())
    }

    single<AppSettingsStore> {
        SharedPreferencesAppSettingsStore(context = get())
    }

    single { GameHistoryStore() }
}
