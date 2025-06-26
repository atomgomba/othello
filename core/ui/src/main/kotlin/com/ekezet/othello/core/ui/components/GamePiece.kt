package com.ekezet.othello.core.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.color

@Composable
fun GamePiece(disk: Disk, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.then(
            Modifier.aspectRatio(1F),
        ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = disk,
            transitionSpec = {
                scaleIn(animationSpec = spring()) togetherWith scaleOut(animationSpec = spring())
            },
            contentAlignment = Alignment.Center,
        ) { state ->
            Canvas(modifier = Modifier.fillMaxSize(.8F)) {
                drawCircle(color = state.color, radius = size.minDimension / 2)
            }
        }
    }
}

@Composable
fun GamePieceWithBorder(disk: Disk, modifier: Modifier = Modifier) {
    GamePiece(
        disk = disk,
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
            shape = CircleShape,
        ),
    )
}
