package com.ekezet.othello.di

import com.ekezet.othello.MainDependency
import com.ekezet.othello.feature.gameboard.di.gameBoardFeatureModule
import com.ekezet.othello.ui.MainActivity
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal fun mainModule(mainActivity: MainActivity) = module {
    includes(
        gameBoardFeatureModule(),
    )

    single { mainActivity }

    singleOf(::MainDependency)
}
