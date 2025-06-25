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
import com.ekezet.othello.core.ui.color
import kotlinx.coroutines.launch

@Composable
internal fun NextMoveIndicator(disk: Disk) {
    val scope = rememberCoroutineScope()
    val size = remember { Animatable(initialValue = 0F) }

    Surface(
        shape = CircleShape,
        color = disk.color,
        modifier = Modifier.fillMaxSize(size.value),
    ) {}

    SideEffect {
        scope.launch {
            size.animateTo(.8F, spring())
        }
    }
}
