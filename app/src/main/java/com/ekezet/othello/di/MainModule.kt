package com.ekezet.othello.di

import com.ekezet.othello.MainDependency
import com.ekezet.othello.core.game.di.gameCoreModule
import com.ekezet.othello.feature.gamesettings.di.gameSettingsFeatureModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun mainModule() = module {
    includes(
        gameCoreModule(),
        gameSettingsFeatureModule(),
    )

    //single { mainActivity }

    singleOf(::MainDependency)
}
