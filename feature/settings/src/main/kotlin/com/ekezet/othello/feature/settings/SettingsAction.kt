package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.strategy.preferSides

internal sealed interface SettingsAction : Action<SettingsModel, SettingsDependency>

internal data class OnStrategySelectorClicked(val player: Disk) : SettingsAction {
    override fun SettingsModel.proceed() = next(showStrategySelectorFor(player))
}

internal data object OnStrategySelectorDismissed : SettingsAction {
    override fun SettingsModel.proceed() = next(dismissStrategySelector())
}

internal data class OnStrategySelected(
    val disk: Disk,
    val strategy: Strategy?,
) : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = setStrategyFor(disk, strategy)
        return next(updated, PublishGameSettings(updated))
    }
}

internal data class OnPreferSidesToggled(val disk: Disk, val prefer: Boolean) : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = setStrategyFor(disk, setPreferSides(disk, prefer))
        return next(updated, PublishGameSettings(updated))
    }

    private fun SettingsModel.setPreferSides(disk: Disk, prefer: Boolean): Strategy? {
        val strategy = if (disk.isDark) darkStrategy else lightStrategy
        return if (prefer) {
            strategy?.preferSides()
        } else {
            if (strategy is DecoratedStrategy) strategy.wrapped else strategy
        }
    }
}

internal data object OnShowPossibleMovesClicked : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleShowPossibleMoves()
        return next(updated, PublishGameSettings(updated))
    }
}

internal data object OnShowBoardPositionsClicked : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleShowBoardPositions()
        return next(updated, PublishGameSettings(updated))
    }
}

internal data object OnGrayscaleModeClicked : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleGrayScaleMode()
        return next(updated, PublishGameSettings(updated))
    }
}

internal data object OnAlwaysScrollToBottomClicked : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleAlwaysScrollToBottom()
        return next(updated, PublishHistorySettings(updated))
    }
}

internal data object OnConfirmExitClicked : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleConfirmExit()
        return next(updated, PublishAppSettings(updated))
    }
}

internal data object OnResetSettingsClicked : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> =
        next(copy(showConfirmResetSettingsDialog = true))
}

internal data class OnResetSettingsDialogClicked(val isConfirmed: Boolean) : SettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val effects = buildList {
            if (isConfirmed) {
                add(ResetAppSettings)
            }
        }
        return next(copy(showConfirmResetSettingsDialog = false), effects = effects.toTypedArray())
    }
}
