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
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class GameSettingsModel(
    override val lightStrategy: Strategy? = defaultLightStrategy,
    override val darkStrategy: Strategy? = defaultDarkStrategy,
    override val displayOptions: BoardDisplayOptions = defaultDisplayOptions,
    internal val selectingStrategyFor: Disk? = null,
) : IGameSettings {
    internal fun resetSelection() = copy(selectingStrategyFor = null)
}

@Stable
internal data class GameSettingsState(
    internal val isLightPreferSides: Boolean,
    internal val isDarkPreferSides: Boolean,
    internal val displayOptions: BoardDisplayOptions,
    internal val lightStrategy: Strategy? = defaultLightStrategy,
    internal val darkStrategy: Strategy? = defaultDarkStrategy,
    internal val selectingStrategyFor: Disk?,
    internal val onShowStrategiesClick: (disk: Disk) -> Unit,
    internal val onDismissStrategies: () -> Unit,
    internal val onPreferSidesClick: (disk: Disk, prefer: Boolean) -> Unit,
    internal val onStrategySelect: (disk: Disk, strategy: Strategy?) -> Unit,
    internal val onShowPossibleMovesClick: () -> Unit,
    internal val onShowBoardPositionsClick: () -> Unit,
) {
    internal val Disk.isNotHuman: Boolean
        inline get() = if (isLight) {
            lightName != null
        } else {
            darkName != null
        }

    internal val lightName: String?
        inline get() = lightStrategy?.name

    internal val darkName: String?
        inline get() = darkStrategy?.name

    internal fun Disk.isStrategySelected(strategy: Strategy?) = isStrategySelected(
        if (isLight) {
            lightStrategy
        } else {
            darkStrategy
        },
        strategy,
    )

    private fun isStrategySelected(current: Strategy?, other: Strategy?) =
        current == other || (current is DecoratedStrategy && current.wrapped == other)
}

internal class GameSettingsDependency(
    gameSettingsStore: GameSettingsStore? = null,
) : KoinComponent, HasGameSettingsStore {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
}
