package com.ekezet.othello.feature.gameboard.ui.components.overlay

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.color
import com.ekezet.othello.core.ui.render.moveHighlightColor

@Composable
internal fun PastMoveIndicator(disk: Disk) {
    Surface(
        shape = CircleShape,
        color = disk.color,
        modifier = Modifier
            .fillMaxSize(.8F)
            .border(width = 3.dp, color = moveHighlightColor, CircleShape),
    ) {}
}
