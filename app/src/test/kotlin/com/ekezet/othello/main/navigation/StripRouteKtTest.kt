package com.ekezet.othello.main.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StripRouteKtTest {

    @Test
    fun stripRoute() {
        assertEquals("route", "route?param1=123&param2=abc".stripRoute())
    }
}
