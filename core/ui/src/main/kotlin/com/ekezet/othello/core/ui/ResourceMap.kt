package com.ekezet.othello.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.ui.R.string

val Disk.stringResource: String
    @Composable inline get() = stringResource(
        if (this == Disk.Dark) {
            string.common__dark
        } else {
            string.common__light
        },
    )

val Strategy?.stringResource: String
    @Composable inline get() =
        this?.name ?: stringResource(string.common__human_player)
