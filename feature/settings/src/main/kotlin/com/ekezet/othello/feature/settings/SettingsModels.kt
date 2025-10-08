package com.ekezet.othello.feature.settings

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.ArgsApplyer
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.data.AppSettings
import com.ekezet.othello.core.game.data.BoardDisplayOptions
import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.DefaultConfirmExit
import com.ekezet.othello.core.game.data.DefaultDarkStrategy
import com.ekezet.othello.core.game.data.DefaultLightStrategy
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.core.game.data.HistoryDisplayOptions
import com.ekezet.othello.core.game.data.HistorySettings
import com.ekezet.othello.core.game.data.IAppSettings
import com.ekezet.othello.core.game.data.IGameSettings
import com.ekezet.othello.core.game.data.IHistorySettings
import com.ekezet.othello.core.game.dependency.AppSettingsPublisher
import com.ekezet.othello.core.game.dependency.GameSettingsPublisher
import com.ekezet.othello.core.game.dependency.HistorySettingsPublisher
import com.ekezet.othello.core.game.store.AppSettingsStore
import com.ekezet.othello.core.game.store.GameHistoryStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.store.HistorySettingsStore
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal data class SettingsModel(
    override val lightStrategy: Strategy? = DefaultLightStrategy,
    override val darkStrategy: Strategy? = DefaultDarkStrategy,
    override val boardDisplayOptions: BoardDisplayOptions = BoardDisplayOptions.Default,
    override val historyDisplayOptions: HistoryDisplayOptions = HistoryDisplayOptions.Default,
    override val confirmExit: Boolean = DefaultConfirmExit,
    internal val selectingStrategyFor: Disk? = null,
    internal val showConfirmResetSettingsDialog: Boolean = false,
) : IGameSettings, IHistorySettings, IAppSettings {
    internal fun showStrategySelectorFor(player: Disk) = copy(selectingStrategyFor = player)
    internal fun dismissStrategySelector() = copy(selectingStrategyFor = null)
    internal fun setStrategyFor(player: Disk, strategy: Strategy?) =
        if (player.isDark) {
            copy(darkStrategy = strategy)
        } else {
            copy(lightStrategy = strategy)
        }
            .dismissStrategySelector()

    internal fun toggleShowBoardPositions() = copy(
        boardDisplayOptions = boardDisplayOptions.copy(
            showBoardPositions = !boardDisplayOptions.showBoardPositions,
        ),
    )

    internal fun toggleShowPossibleMoves() = copy(
        boardDisplayOptions = boardDisplayOptions.copy(
            showPossibleMoves = !boardDisplayOptions.showPossibleMoves,
        ),
    )

    internal fun toggleGrayScaleMode() = copy(
        boardDisplayOptions = boardDisplayOptions.copy(
            isGrayscaleMode = !boardDisplayOptions.isGrayscaleMode,
        ),
    )

    internal fun toggleAlwaysScrollToBottom() = copy(
        historyDisplayOptions = historyDisplayOptions.copy(
            alwaysScrollToBottom = !historyDisplayOptions.alwaysScrollToBottom,
        ),
    )

    internal fun toggleConfirmExit() = copy(confirmExit = !confirmExit)
}

@Immutable
internal data class SettingsState(
    val isLightPreferSides: Boolean,
    val isDarkPreferSides: Boolean,
    val boardDisplayOptions: BoardDisplayOptions,
    val historyDisplayOptions: HistoryDisplayOptions,
    val lightStrategy: Strategy?,
    val darkStrategy: Strategy?,
    val selectingStrategyFor: Disk?,
    val confirmExit: Boolean,
    val showConfirmResetSettingsDialog: Boolean,
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

@Stable
data class SettingsArgs(
    val gameSettings: GameSettings,
    val historySettings: HistorySettings,
    val appSettings: AppSettings,
)

internal class SettingsDependency(
    gameSettingsStore: GameSettingsStore? = null,
    historySettingsStore: HistorySettingsStore? = null,
    appSettingsStore: AppSettingsStore? = null,
    gameHistoryStore: GameHistoryStore? = null,
) : KoinComponent, GameSettingsPublisher, HistorySettingsPublisher, AppSettingsPublisher {
    override val gameSettingsStore: GameSettingsStore = gameSettingsStore ?: get()
    override val historySettingsStore: HistorySettingsStore = historySettingsStore ?: get()
    override val appSettingsStore: AppSettingsStore = appSettingsStore ?: get()
    override val gameHistoryStore: GameHistoryStore = gameHistoryStore ?: get()
}

internal typealias SettingsEmitter = ActionEmitter<SettingsModel, SettingsDependency>

internal typealias SettingsArgsApplyer = ArgsApplyer<SettingsModel, SettingsArgs>
