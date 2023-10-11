package com.ekezet.othello.core.game.store

import android.content.Context
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategies
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.strategy.wrappedName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DefaultGameSettingsStore(
    context: Context,
) : GameSettingsStore {
    private val prefs = context.getSharedPreferences("game-settings", Context.MODE_PRIVATE)

    private val _settings: MutableStateFlow<GameSettings> = MutableStateFlow(load())
    override val settings: StateFlow<GameSettings>
        get() = _settings.asStateFlow()

    override suspend fun update(new: IGameSettings) {
        val data = GameSettings.from(new)
        _settings.value = data
        persist(data)
    }

    private fun persist(data: IGameSettings) = with(prefs.edit()) {
        putString(KEY_LIGHT_NAME, data.lightStrategy?.wrappedName)
        putBoolean(KEY_LIGHT_PREFER_SIDES, data.lightStrategy is PreferSidesDecoratorStrategy)
        putString(KEY_DARK_NAME, data.darkStrategy?.wrappedName)
        putBoolean(KEY_DARK_PREFER_SIDES, data.darkStrategy is PreferSidesDecoratorStrategy)

        putBoolean(KEY_SHOW_POSSIBLE_MOVES, data.displayOptions.showPossibleMoves)
        putBoolean(KEY_SHOW_BOARD_POSITIONS, data.displayOptions.showBoardPositions)
        putBoolean(KEY_IS_GRAYSCALE_MODE, data.displayOptions.isGrayscaleMode)

        apply()
    }

    private fun load(): GameSettings = with(prefs) {
        val lightStrategy = getString(KEY_LIGHT_NAME, defaultLightStrategy.wrappedName)
            ?.let { findStrategyByName(it) }
            ?.run {
                if (getBoolean(
                        KEY_LIGHT_PREFER_SIDES, defaultLightStrategy is PreferSidesDecoratorStrategy
                    )
                ) preferSides() else this
            }
        val darkStrategy = getString(KEY_DARK_NAME, defaultDarkStrategy.wrappedName)
            ?.let { findStrategyByName(it) }
            ?.run {
                if (getBoolean(
                        KEY_DARK_PREFER_SIDES, defaultDarkStrategy is PreferSidesDecoratorStrategy
                    )
                ) preferSides() else this
            }
        val displayOptions = BoardDisplayOptions(
            showPossibleMoves = getBoolean(
                KEY_SHOW_POSSIBLE_MOVES, defaultDisplayOptions.showPossibleMoves
            ),
            showBoardPositions = getBoolean(
                KEY_SHOW_BOARD_POSITIONS, defaultDisplayOptions.showBoardPositions
            ),
            isGrayscaleMode = getBoolean(
                KEY_IS_GRAYSCALE_MODE, defaultDisplayOptions.isGrayscaleMode
            ),
        )
        return GameSettings(
            lightStrategy = lightStrategy,
            darkStrategy = darkStrategy,
            displayOptions = displayOptions,
        )
    }

    private fun findStrategyByName(name: String): Strategy? =
        Strategies.firstOrNull { it?.name == name }

    companion object {
        private const val KEY_LIGHT_NAME = "lightName"
        private const val KEY_LIGHT_PREFER_SIDES = "lightPreferSides"
        private const val KEY_DARK_NAME = "darkName"
        private const val KEY_DARK_PREFER_SIDES = "darkPreferSides"
        private const val KEY_SHOW_POSSIBLE_MOVES = "showPossibleMoves"
        private const val KEY_SHOW_BOARD_POSITIONS = "showBoardPositions"
        private const val KEY_IS_GRAYSCALE_MODE = "isGrayScaleMode"
    }
}
