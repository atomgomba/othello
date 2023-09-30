package com.ekezet.othello.feature.gameboard.ui.components.overlay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun NextMoveIndicator() {
    Surface(
        shape = CircleShape,
        color = Color.Yellow.copy(alpha = .6F),
        modifier = Modifier.fillMaxSize(.5F),
    ) {}
}
