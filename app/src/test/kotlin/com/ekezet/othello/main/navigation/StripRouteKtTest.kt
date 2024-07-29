package com.ekezet.othello.main.navigation

import kotlin.test.assertEquals
import kotlin.test.Test

internal class StripRouteKtTest {

    @Test
    fun stripRoute() {
        assertEquals("route", "route?param1=123&param2=abc".stripRoute())
    }
}
