package com.ekezet.othello.feature.settings.feature.strategyPicker

import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.isDark
import com.ekezet.othello.core.game.strategy.Strategy

internal data class PublishSelectedStrategy(
    val disk: Disk,
    val strategy: Strategy?,
) : StrategyPickerEffect {
    override suspend fun StrategyPickerEmitter.trigger(
        dependency: StrategyPickerDependency?,
    ) = dependency?.run {
        gameSettingsStore.update(
            gameSettingsStore.settings.value.run {
                if (disk.isDark) copy(darkStrategy = strategy) else copy(lightStrategy = strategy)
            }
        )
    }
}
