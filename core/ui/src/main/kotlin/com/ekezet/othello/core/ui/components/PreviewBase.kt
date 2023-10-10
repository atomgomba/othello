package com.ekezet.othello.core.ui.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.ekezet.othello.core.ui.theme.OthelloTheme

@Composable
fun PreviewBase(content: @Composable () -> Unit) {
    OthelloTheme {
        Surface {
            content()
        }
    }
}
