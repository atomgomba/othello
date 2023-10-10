package com.ekezet.othello.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface MainDestinations {
    val label: String
    val icon: ImageVector

    companion object {
        val Start = GameBoard

        val All = listOf(
            GameBoard,
            GameSettings,
        )
    }

    data object GameBoard : MainDestinations {
        override val label: String = "game-board"
        override val icon: ImageVector = Icons.Default.PlayArrow
    }

    data object GameSettings : MainDestinations {
        override val label: String = "game-settings"
        override val icon: ImageVector = Icons.Default.Settings

        const val PICK_STRATEGY = "pickStrategy"
    }
}
