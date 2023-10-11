package com.ekezet.othello.feature.gameboard.ui.components.overlay

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.components.color
import kotlinx.coroutines.launch

@Composable
internal fun NextMoveIndicator(disk: Disk) {
    val scope = rememberCoroutineScope()
    val progress = remember { Animatable(initialValue = 0F) }

    Surface(
        shape = CircleShape,
        color = disk.color.copy(alpha = .6F),
        modifier = Modifier.fillMaxSize(progress.value),
    ) {}

    SideEffect {
        scope.launch {
            progress.animateTo(.5F, spring())
        }
    }
}
