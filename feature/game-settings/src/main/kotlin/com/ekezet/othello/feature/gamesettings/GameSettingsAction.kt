package com.ekezet.othello.feature.gamesettings

import com.ekezet.hurok.Action
import com.ekezet.hurok.Action.Next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isLight
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy
import com.ekezet.othello.core.game.strategy.PreferSidesDecoratorStrategy.Companion.preferSides
import com.ekezet.othello.core.game.strategy.Strategy

internal sealed interface GameSettingsAction : Action<GameSettingsModel, GameSettingsDependency>

internal data class OnSelectStrategyClicked(val disk: Disk) : GameSettingsAction {
    override fun GameSettingsModel.proceed() =
        change(copy(selectingStrategyFor = disk))
}

internal data object OnSelectStrategyDismissed : GameSettingsAction {
    override fun GameSettingsModel.proceed() =
        change(copy(selectingStrategyFor = null))
}

internal data class OnStrategyItemClicked(val disk: Disk, val strategy: Strategy?) :
    GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameSettingsDependency> {
        val updated = if (disk.isLight) {
            copy(lightStrategy = strategy)
        } else {
            copy(darkStrategy = strategy)
        }
        return outcome(
            updated.copy(selectingStrategyFor = null),
            PublishGameSettings(updated),
        )
    }
}

internal data class OnPreferSidesClicked(val disk: Disk, val prefer: Boolean) : GameSettingsAction {
    override fun GameSettingsModel.proceed(): Next<GameSettingsModel, GameSettingsDependency> {
        val updated = if (disk.isLight) {
            copy(lightStrategy = setPreferredStrategy(prefer, lightStrategy))
        } else {
            copy(darkStrategy = setPreferredStrategy(prefer, darkStrategy))
        }
        return outcome(updated, PublishGameSettings(updated))
    }

    private fun setPreferredStrategy(prefer: Boolean, strategy: Strategy?): Strategy? = if (prefer) {
        strategy?.preferSides()
    } else {
        if (strategy is PreferSidesDecoratorStrategy) strategy.wrapped else strategy
    }
}
