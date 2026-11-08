package com.ekezet.othello.main.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ekezet.othello.core.data.ExcludeFromCoverage
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.valueOf
import com.ekezet.othello.core.ui.R
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@ExcludeFromCoverage
@Serializable
sealed class Screen {
    abstract val icon: ImageVector
    abstract val labelRes: Int

    @ExcludeFromCoverage
    companion object {
        val Start = GameBoard()
        val All = listOf(
            GameBoard(),
            GameHistory,
            Settings(),
        )

        fun classOf(route: String?): KClass<out Screen>? =
            when {
                route == null -> null
                route.contains(GameBoard::class.simpleName!!) -> GameBoard::class
                route.contains(GameHistory::class.simpleName!!) -> GameHistory::class
                route.contains(Settings::class.simpleName!!) -> Settings::class
                else -> null
            }
    }

    @ExcludeFromCoverage
    @Serializable
    data class GameBoard(val showTurn: Int? = null) : Screen() {
        override val icon: ImageVector get() = Icons.Default.PlayArrow
        override val labelRes: Int = R.string.main__nav__game_board
    }

    @ExcludeFromCoverage
    @Serializable
    data object GameHistory : Screen() {
        override val icon: ImageVector get() = Icons.Default.History
        override val labelRes: Int = R.string.main__nav__game_history
    }

    @ExcludeFromCoverage
    @Serializable
    data class Settings(val pickStrategyFor: String? = null) : Screen() {
        override val icon: ImageVector get() = Icons.Default.Settings
        override val labelRes: Int = R.string.main__nav__settings
        val pickStrategyForDisk: Disk?
            get() = Disk.valueOf(pickStrategyFor)
    }
}
