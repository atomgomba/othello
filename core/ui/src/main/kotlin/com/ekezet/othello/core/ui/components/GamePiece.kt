package com.ekezet.othello.core.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark

internal val DarkColor = Color.Black
internal val LightColor = Color.White

@Composable
fun GamePiece(disk: Disk, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.then(
            Modifier.aspectRatio(1F),
        ),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(.8F),
            shape = CircleShape,
            color = disk.color,
        ) {}
    }
}

@Composable
fun GamePieceWithBorder(disk: Disk, modifier: Modifier = Modifier) {
    val bordered = modifier.border(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outline,
        shape = CircleShape,
    )

    GamePiece(disk = disk, modifier = bordered)
}

val Disk.color: Color
    get() = if (isDark) DarkColor else LightColor
