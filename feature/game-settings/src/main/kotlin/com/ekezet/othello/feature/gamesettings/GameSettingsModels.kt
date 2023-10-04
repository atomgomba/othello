package com.ekezet.othello.feature.gamesettings

import androidx.compose.runtime.Stable
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.dependency.HasGameSettingsStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class GameSettingsModel(
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
    val selectingStrategyFor: Disk? = null,
) : IGameSettings

@Stable
internal data class GameSettingsState(
    val isLightPreferSides: Boolean,
    val isDarkPreferSides: Boolean,
    val lightStrategy: Strategy? = defaultLightStrategy,
    val darkStrategy: Strategy? = defaultDarkStrategy,
    val selectingStrategyFor: Disk?,
    val onShowStrategiesClick: (disk: Disk) -> Unit,
    val onDismissStrategies: () -> Unit,
    val onPreferSidesClick: (disk: Disk, prefer: Boolean) -> Unit,
    val onStrategySelect: (disk: Disk, strategy: Strategy?) -> Unit,
) {
    val Disk.isNotHuman: Boolean
        get() = if (isLight) {
            lightName != null
        } else {
            darkName != null
        }

    val lightName: String?
        get() = lightStrategy?.name

    val darkName: String?
        get() = darkStrategy?.name

    fun Disk.isStrategySelected(strategy: Strategy?) = isStrategySelected(
        if (isLight) {
            lightStrategy
        } else {
            darkStrategy
        },
        strategy,
    )

    private fun isStrategySelected(current: Strategy?, other: Strategy?) =
        current == other || (current is PreferSidesDecoratorStrategy && current.wrapped == other)
}

internal class GameSettingsDependency(
    gameSettingsStore: GameSettingsStore? = null,
) : KoinComponent, HasGameSettingsStore {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
}
