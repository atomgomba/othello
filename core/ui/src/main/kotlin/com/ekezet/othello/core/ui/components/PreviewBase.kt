package com.ekezet.othello.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ekezet.othello.core.ui.theme.OthelloTheme

@Composable
fun PreviewBase(content: @Composable () -> Unit) {
    OthelloTheme {
        Surface {
            Box(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}
