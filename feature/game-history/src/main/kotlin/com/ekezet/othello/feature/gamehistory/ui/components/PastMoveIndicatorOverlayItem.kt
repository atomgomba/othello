package com.ekezet.othello.feature.gamehistory.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ekezet.othello.core.ui.viewModels.Sprite

internal object PastMoveIndicator : Sprite {
    @Composable
    override fun Composable() {
        Surface(
            shape = CircleShape,
            color = Color(255, 98, 0),
            modifier = Modifier.fillMaxSize(.45F),
        ) {}
    }

    override val composeKey: Any? = null
}
