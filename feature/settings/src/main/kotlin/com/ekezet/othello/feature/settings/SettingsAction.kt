package com.ekezet.othello.feature.settings

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy

internal sealed interface GameSettingsAction : Action<SettingsModel, SettingsDependency>

internal data class OnStrategySelectorClicked(val player: Disk) : GameSettingsAction {
    override fun SettingsModel.proceed() = mutate(showStrategySelectorFor(player))
}

internal data object OnStrategySelectorDismissed : GameSettingsAction {
    override fun SettingsModel.proceed() = mutate(dismissStrategySelector())
}

internal data class OnStrategySelected(
    val disk: Disk,
    val strategy: Strategy?,
) : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = setStrategyFor(disk, strategy)
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data class OnPreferSidesToggled(val disk: Disk, val prefer: Boolean) : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = setStrategyFor(disk, setPreferSides(disk, prefer))
        return outcome(updated, PublishGameSettings(updated))
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

internal data object OnShowPossibleMovesClicked : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleShowPossibleMoves()
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data object OnShowBoardPositionsClicked : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleShowBoardPositions()
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data object OnGrayscaleModeClicked : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleGrayScaleMode()
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data object OnAlwaysScrollToBottomClicked : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleAlwaysScrollToBottom()
        return outcome(updated, PublishHistorySettings(updated))
    }
}

internal data object OnConfirmExitClicked : GameSettingsAction {
    override fun SettingsModel.proceed(): Next<SettingsModel, SettingsDependency> {
        val updated = toggleConfirmExit()
        return outcome(updated, PublishAppSettings(updated))
    }
}
