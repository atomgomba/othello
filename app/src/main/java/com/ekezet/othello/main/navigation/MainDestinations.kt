package com.ekezet.othello.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface MainDestinations {
    val id: String
    val icon: ImageVector

    companion object {
        val Start: String = GameBoardDestination.id
        val All = listOf(
            GameBoardDestination,
            GameSettingsDestination,
        )
    }

    data object GameBoardDestination : MainDestinations {
        override val id: String = "game-board"
        override val icon: ImageVector = Icons.Default.PlayArrow
    }

    data object GameSettingsDestination : MainDestinations {
        override val id: String = "game-settings"
        override val icon: ImageVector = Icons.Default.Settings

        const val PickStrategy = "pickStrategy"
    }
}
