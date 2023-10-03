package com.ekezet.othello.core.game.di

import com.ekezet.othello.core.game.store.DefaultGameSettingsStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import org.koin.dsl.module

fun gameCoreModule() = module {
    single<GameSettingsStore> { DefaultGameSettingsStore() }
}
