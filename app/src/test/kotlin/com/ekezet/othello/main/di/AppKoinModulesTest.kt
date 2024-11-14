package com.ekezet.othello.main.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

@OptIn(KoinExperimentalAPI::class)
internal class AppKoinModulesTest {
    @Test
    fun verify() {
        mainModule().verify()
    }
}
