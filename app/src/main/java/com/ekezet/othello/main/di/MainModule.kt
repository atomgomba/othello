package com.ekezet.othello.main.di

import com.ekezet.othello.core.game.di.gameCoreModule
import com.ekezet.othello.feature.gameboard.di.gameBoardFeatureModule
import com.ekezet.othello.feature.gamesettings.di.gameSettingsFeatureModule
import com.ekezet.othello.main.MainDependency
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun mainModule() = module {
    includes(
        gameCoreModule(),
        gameBoardFeatureModule(),
        gameSettingsFeatureModule(),
    )

    singleOf(::MainDependency)
}
