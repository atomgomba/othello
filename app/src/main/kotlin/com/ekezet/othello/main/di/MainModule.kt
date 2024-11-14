package com.ekezet.othello.main.di

import com.ekezet.othello.core.game.di.gameCoreModule
import com.ekezet.othello.core.ui.di.uiCoreModule
import org.koin.dsl.module

internal fun mainModule() = module {
    includes(
        uiCoreModule(),
        gameCoreModule(),
    )
}
