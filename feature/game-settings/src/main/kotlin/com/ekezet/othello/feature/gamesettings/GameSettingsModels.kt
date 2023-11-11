package com.ekezet.othello.feature.gamesettings

import androidx.compose.runtime.Immutable
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.defaultDarkStrategy
import com.ekezet.othello.core.game.data.defaultDisplayOptions
import com.ekezet.othello.core.game.data.defaultLightStrategy
import com.ekezet.othello.core.game.store.HasGameSettingsStore
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
    internal fun showStrategySelectorFor(player: Disk) = copy(selectingStrategyFor = player)
    internal fun dismissStrategySelector() = copy(selectingStrategyFor = null)
    internal fun setStrategyFor(player: Disk, strategy: Strategy?) =
        if (player.isDark) {
            copy(darkStrategy = strategy)
        } else {
            copy(lightStrategy = strategy)
        }
            .dismissStrategySelector()
}

@Immutable
internal data class GameSettingsStateActions(
    val onStrategySelectorClick: (disk: Disk) -> Unit,
    val onStrategySelectorDismiss: () -> Unit,
    val onPreferSidesToggle: (disk: Disk, prefer: Boolean) -> Unit,
    val onStrategySelect: (disk: Disk, strategy: Strategy?) -> Unit,
    val onShowPossibleMovesClick: () -> Unit,
    val onShowBoardPositionsClick: () -> Unit,
    val onGrayscaleModeClick: () -> Unit,
)

@Immutable
internal data class GameSettingsState(
    val actions: GameSettingsStateActions,
    val isLightPreferSides: Boolean,
    val isDarkPreferSides: Boolean,
    val displayOptions: BoardDisplayOptions,
    val lightStrategy: Strategy? = defaultLightStrategy,
    val darkStrategy: Strategy? = defaultDarkStrategy,
    val selectingStrategyFor: Disk?,
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
