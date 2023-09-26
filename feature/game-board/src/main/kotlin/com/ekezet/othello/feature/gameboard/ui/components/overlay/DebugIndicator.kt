package com.ekezet.othello.feature.gameboard.ui.components.overlay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun DebugIndicator(color: Color) {
    Surface(
        shape = CircleShape,
        color = Color.Unspecified,
        modifier = Modifier
            .size(16.dp, 16.dp)
            .border(1.dp, Color.White, CircleShape)
            .padding(1.dp)
            .border(1.dp, color.copy(alpha = .8F), CircleShape)
            .padding(1.dp)
            .border(1.dp, Color.Black, CircleShape),
    ) {}
}
