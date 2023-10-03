package com.ekezet.othello.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                .fillMaxSize(.8F),
            shape = CircleShape,
            color = disk.color,
        ) {}
    }
}

val Disk.color: Color
    get() = if (isDark) DarkColor else LightColor
