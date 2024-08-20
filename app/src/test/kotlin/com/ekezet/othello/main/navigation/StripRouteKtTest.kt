package com.ekezet.othello.main.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

internal class StripRouteKtTest {

    @Test
    fun stripRoute() {
        assertEquals("route", "route?param1=123&param2=abc".stripRoute())
    }
}
