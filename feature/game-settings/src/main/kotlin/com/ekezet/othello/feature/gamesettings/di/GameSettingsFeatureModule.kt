package com.ekezet.othello.feature.gamesettings.di

import com.ekezet.othello.feature.gamesettings.GameGameSettingsDependency
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun gameSettingsFeatureModule() = module {
    singleOf(::GameGameSettingsDependency)
}
