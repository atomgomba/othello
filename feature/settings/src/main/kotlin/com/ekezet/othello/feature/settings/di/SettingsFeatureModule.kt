package com.ekezet.othello.feature.settings.di

import com.ekezet.othello.feature.settings.SettingsDependency
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun settingsFeatureModule() = module {
    singleOf(::SettingsDependency)
}
