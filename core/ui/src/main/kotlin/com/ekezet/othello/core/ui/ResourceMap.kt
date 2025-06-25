package com.ekezet.othello.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.ui.R.string

val Disk.stringResource: String
    @Composable inline get() = stringResource(
        if (isDark) {
            string.common__dark
        } else {
            string.common__light
        },
    )

val Disk.color: Color
    get() = if (isDark) Color.Companion.Black else Color.Companion.White

val Strategy?.nameOrHumanPlayer: String
    @Composable inline get() =
        this?.name.orHumanPlayer

val String?.orHumanPlayer: String
    @Composable inline get() = this ?: stringResource(id = string.common__human_player)
