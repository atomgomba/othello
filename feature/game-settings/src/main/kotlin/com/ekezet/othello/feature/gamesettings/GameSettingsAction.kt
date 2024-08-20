package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.strategy.DecoratedStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy

internal sealed interface GameSettingsAction : Action<GameSettingsModel, GameGameSettingsDependency>

internal data class OnStrategySelectorClicked(val player: Disk) : GameSettingsAction {
    override fun GameSettingsModel.proceed() = mutate(showStrategySelectorFor(player))
}

internal data object OnStrategySelectorDismissed : GameSettingsAction {
    override fun GameSettingsModel.proceed() = mutate(dismissStrategySelector())
}

internal data class OnStrategySelected(
    val disk: Disk,
    val strategy: Strategy?,
) : GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameGameSettingsDependency> {
        val updated = setStrategyFor(disk, strategy)
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data class OnPreferSidesToggled(val disk: Disk, val prefer: Boolean) : GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameGameSettingsDependency> {
        val updated = setStrategyFor(disk, setPreferSides(disk, prefer))
        return outcome(updated, PublishGameSettings(updated))
    }

    private fun GameSettingsModel.setPreferSides(disk: Disk, prefer: Boolean): Strategy? {
        val strategy = if (disk.isDark) darkStrategy else lightStrategy
        return if (prefer) {
            strategy?.preferSides()
        } else {
            if (strategy is DecoratedStrategy) strategy.wrapped else strategy
        }
    }
}

internal data object OnShowPossibleMovesClicked : GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameGameSettingsDependency> {
        val updated = copy(
            displayOptions = displayOptions.copy(
                showPossibleMoves = !displayOptions.showPossibleMoves,
            ),
        )
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data object OnShowBoardPositionsClicked : GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameGameSettingsDependency> {
        val updated = copy(
            displayOptions = displayOptions.copy(
                showBoardPositions = !displayOptions.showBoardPositions,
            ),
        )
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data object OnGrayscaleModeClicked : GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameGameSettingsDependency> {
        val updated = copy(
            displayOptions = displayOptions.copy(
                isGrayscaleMode = !displayOptions.isGrayscaleMode,
            ),
        )
        return outcome(updated, PublishGameSettings(updated))
    }
}

internal data object OnConfirmExitClicked: GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameGameSettingsDependency> {
        val updated = toggleConfirmExit()
        return outcome(updated, PublishGameSettings(updated))
    }
}
