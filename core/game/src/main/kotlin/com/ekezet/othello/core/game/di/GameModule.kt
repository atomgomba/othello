package com.ekezet.othello.core.game.di

import com.ekezet.othello.core.game.provider.DefaultGameSettingsProvider
import com.ekezet.othello.core.game.provider.GameSettingsProvider
import org.koin.dsl.module

fun gameCoreModule() = module {
    single<GameSettingsProvider> { DefaultGameSettingsProvider() }
}
