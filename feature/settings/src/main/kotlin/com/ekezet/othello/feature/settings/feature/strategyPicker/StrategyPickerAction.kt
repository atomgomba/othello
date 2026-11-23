package com.ekezet.othello.feature.settings.feature.strategyPicker

import com.ekezet.hurok.next
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.strategy.Strategy

fun showStrategyPickerFor(player: Disk, default: Strategy?) =
    StrategyPickerAction { model ->
        next(model.copy(selectFor = player, selected = default, visible = true))
    }

internal fun selectStrategy(strategy: Strategy?) =
    StrategyPickerAction { model ->
        next(model.copy(visible = false), setOf(PublishSelectedStrategy(model.selectFor, strategy)))
    }

internal val hideStrategyPicker =
    StrategyPickerAction { model ->
        next(model.copy(visible = false))
    }
