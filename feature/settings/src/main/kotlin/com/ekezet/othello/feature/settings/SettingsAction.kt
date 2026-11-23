package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.hurok.next
import com.ekezet.hurok.trigger
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.Strategy
import com.ekezet.othello.core.game.strategy.preferSides

internal sealed interface SettingsAction : Action<SettingsModel, SettingsDependency>

internal data class OnStrategySelectorClicked(val disk: Disk) : SettingsAction {
    override fun proceed(model: SettingsModel) = with(model) {
        trigger(
            ShowStrategyPicker(
                player = disk,
                current = if (disk.isDark) darkStrategy else lightStrategy
            ),
        )
    }
}

internal data class OnStrategySelected(val disk: Disk, val strategy: Strategy?) : SettingsAction {
    override fun proceed(model: SettingsModel) = with(model) {
        val updated = setStrategyFor(disk, strategy)
        next(updated)
    }
}

internal data class OnPreferSidesToggled(val disk: Disk, val prefer: Boolean) : SettingsAction {
    override fun proceed(model: SettingsModel) = with(model) {
        val updated = setStrategyFor(disk, setPreferSides(disk, prefer))
        next(updated, PublishSettings(updated))
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
    override fun proceed(model: SettingsModel): Next<SettingsModel, SettingsDependency> {
        val updated = model.toggleShowPossibleMoves()
        return next(updated, PublishSettings(updated))
    }
}

internal data object OnShowBoardPositionsClicked : SettingsAction {
    override fun proceed(model: SettingsModel): Next<SettingsModel, SettingsDependency> {
        val updated = model.toggleShowBoardPositions()
        return next(updated, PublishSettings(updated))
    }
}

internal data object OnGrayscaleModeClicked : SettingsAction {
    override fun proceed(model: SettingsModel): Next<SettingsModel, SettingsDependency> {
        val updated = model.toggleGrayScaleMode()
        return next(updated, PublishSettings(updated))
    }
}

internal data object OnAlwaysScrollToBottomClicked : SettingsAction {
    override fun proceed(model: SettingsModel): Next<SettingsModel, SettingsDependency> {
        val updated = model.toggleAlwaysScrollToBottom()
        return next(updated, PublishHistorySettings(updated))
    }
}

internal data object OnConfirmExitClicked : SettingsAction {
    override fun proceed(model: SettingsModel): Next<SettingsModel, SettingsDependency> {
        val updated = model.toggleConfirmExit()
        return next(updated, PublishAppSettings(updated))
    }
}

internal data object OnResetSettingsClicked : SettingsAction {
    override fun proceed(model: SettingsModel) =
        next(model.copy(showConfirmResetSettingsDialog = true))
}

internal data class OnResetSettingsDialogClicked(val isConfirmed: Boolean) : SettingsAction {
    override fun proceed(model: SettingsModel) = with(model) {
        val effects = buildList {
            if (isConfirmed) {
                add(ResetAppSettings)
            }
        }
        next(copy(showConfirmResetSettingsDialog = false), effects = effects.toTypedArray())
    }
}
