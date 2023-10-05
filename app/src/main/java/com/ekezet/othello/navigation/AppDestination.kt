package com.ekezet.othello.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface AppDestination {
    val label: String
    val icon: ImageVector

    data object GameBoard : AppDestination {
        override val label: String = "game-board"
        override val icon: ImageVector = Icons.Default.PlayArrow
    }

    data object GameSettings : AppDestination {
        override val label: String = "game-settings"
        override val icon: ImageVector = Icons.Default.Settings
    }

    companion object {
        val Start = GameBoard

        val All = listOf(
            GameBoard,
            GameSettings,
        )
    }
}
