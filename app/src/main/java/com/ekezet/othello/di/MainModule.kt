package com.ekezet.othello.di

import com.ekezet.othello.MainDependency
import com.ekezet.othello.core.game.di.gameCoreModule
import com.ekezet.othello.feature.gamesettings.di.gameSettingsFeatureModule
import com.ekezet.othello.ui.MainActivity
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun mainModule(mainActivity: MainActivity) = module {
    includes(
        gameCoreModule(),
        gameSettingsFeatureModule(),
    )

    single { mainActivity }

    singleOf(::MainDependency)
}
