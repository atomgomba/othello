package com.ekezet.othello.feature.gameboard.ui.components.overlay

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.feature.gameboard.ui.components.color

@Composable
internal fun ValidMoveIndicator(disk: Disk) {
    Surface(
        shape = CircleShape,
        color = disk.color,
        modifier = Modifier.fillMaxSize(.2F),
    ) {}
}
