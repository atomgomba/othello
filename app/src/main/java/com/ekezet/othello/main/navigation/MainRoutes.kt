package com.ekezet.othello.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ekezet.othello.core.ui.navigation.Route

sealed class MainRoutes : Route() {
    companion object {
        val Start: String = GameBoardRoute.id
        val All = listOf(
            GameBoardRoute,
            GameSettingsRoute,
        )
    }

    data object GameBoardRoute : MainRoutes() {
        override val id: String = "game-board"
        override val icon: ImageVector = Icons.Default.PlayArrow
    }

    data object GameSettingsRoute : MainRoutes() {
        override val id: String = "game-settings"
        override val icon: ImageVector = Icons.Default.Settings

        const val PickStrategy = "pickStrategy"

        override val spec: String
            get() = "$id?$PickStrategy={$PickStrategy}"

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(PickStrategy) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
            )

        override fun make(params: Map<String, String?>): String =
            "$id?$PickStrategy=${params["disk"]}"
    }
}
