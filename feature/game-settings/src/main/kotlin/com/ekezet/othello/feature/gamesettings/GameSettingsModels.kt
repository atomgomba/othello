package com.ekezet.othello.feature.gamesettings

import androidx.compose.runtime.Immutable
import com.ekezet.hurok.ViewState
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultDarkStrategy
import com.ekezet.othello.core.game.data.DefaultLightStrategy
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.dependency.GameSettingsPublisher
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class GameSettingsModel(
    override val lightStrategy: Strategy? = DefaultLightStrategy,
    override val darkStrategy: Strategy? = DefaultDarkStrategy,
    override val displayOptions: BoardDisplayOptions = BoardDisplayOptions.Default,
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
internal data class GameSettingsState(
    val isLightPreferSides: Boolean,
    val isDarkPreferSides: Boolean,
    val displayOptions: BoardDisplayOptions,
    val lightStrategy: Strategy? = DefaultLightStrategy,
    val darkStrategy: Strategy? = DefaultDarkStrategy,
    val selectingStrategyFor: Disk?,
) : ViewState<GameSettingsModel, GameGameSettingsDependency>() {
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

internal class GameGameSettingsDependency(
    gameSettingsStore: GameSettingsStore? = null,
    gameHistoryStore: GameHistoryStore? = null
) : KoinComponent, GameSettingsPublisher {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
    override val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()
}
