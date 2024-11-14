package com.ekezet.othello.core.ui.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
internal class UiCoreKoinModulesTest {
    @Test
    fun verify() {
        uiCoreModule().verify()
    }
}
