package com.ekezet.othello.core.game.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
internal class GameCoreKoinModulesTest {
    @Test
    fun verify() {
        gameCoreModule().verify()
    }
}
