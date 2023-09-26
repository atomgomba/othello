package com.ekezet.othello.feature.gameboard.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.Disk

internal val DarkColor = Color.Black
internal val LightColor = Color.White

@Composable
fun GamePiece(disk: Disk, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            shape = CircleShape,
            color = disk.color,
        ) {}
    }
}

internal val Disk.color: Color
    get() = if (isDark) DarkColor else LightColor
