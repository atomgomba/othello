package com.ekezet.othello.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ekezet.othello.core.data.ExcludeFromCoverage
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.valueOf
import com.ekezet.othello.core.ui.R
import com.ekezet.othello.core.ui.navigation.Route

@ExcludeFromCoverage
sealed class MainRoutes : Route() {
    @ExcludeFromCoverage
    companion object {
        val Start: String = GameBoardRoute.id
        val All = listOf(
            GameBoardRoute,
            GameHistoryRoute,
            SettingsRoute,
        )
    }

    @ExcludeFromCoverage
    data object GameBoardRoute : MainRoutes() {
        override val id: String = "game-board"
        override val icon: ImageVector = Icons.Default.PlayArrow
        override val labelRes: Int = R.string.main__nav__game_board

        private const val ShowTurn = "showTurn"

        override val spec: String
            get() = "$id?$ShowTurn={$ShowTurn}"

        override val arguments: List<NamedNavArgument>
            get() = listOf(
                navArgument(ShowTurn) {
                    type = NavType.IntType
                    defaultValue = -1
                },
            )

        override fun make(params: Map<String, Any?>): String =
            "$id?$ShowTurn=${params["turn"]}"

        fun make(showTurn: Int): String =
            make(mapOf("turn" to showTurn))

        fun findShowTurn(entry: NavBackStackEntry): Int? =
            entry.arguments?.getInt(ShowTurn)?.takeIf { -1 < it }
    }

    @ExcludeFromCoverage
    data object GameHistoryRoute : MainRoutes() {
        override val id: String = "game-history"
        override val icon: ImageVector = Icons.Default.History
        override val labelRes: Int = R.string.main__nav__game_history
    }

    @ExcludeFromCoverage
    data object SettingsRoute : MainRoutes() {
        override val id: String = "settings"
        override val icon: ImageVector = Icons.Default.Settings
        override val labelRes: Int = R.string.main__nav__settings

        private const val PickStrategy = "pickStrategy"

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

        override fun make(params: Map<String, Any?>): String =
            "$id?$PickStrategy=${params["disk"]}"

        fun make(pickStrategyFor: Disk): String =
            make(mapOf("disk" to pickStrategyFor.toString()))

        fun findPickStrategy(entry: NavBackStackEntry): Disk? =
            Disk.valueOf(
                entry.arguments?.getString(PickStrategy),
            )
    }
}
