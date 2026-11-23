package com.ekezet.othello.feature.settings.feature.strategyPicker

import com.ekezet.hurok.Action
import com.ekezet.hurok.ActionEmitter
import com.ekezet.hurok.Effect
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.game.dependency.HasGameSettingsStore
import com.ekezet.othello.core.game.store.GameSettingsStore
import com.ekezet.othello.core.game.strategy.Strategy

data class StrategyPickerModel(
    val selectFor: Disk = Disk.Dark,
    val selected: Strategy? = null,
    val visible: Boolean = false,
)

data class StrategyPickerState(
    val pickForDisk: Disk,
    val selectedName: String?,
    val visible: Boolean,
)

class StrategyPickerDependency(
    override val gameSettingsStore: GameSettingsStore,
) : HasGameSettingsStore

fun interface StrategyPickerAction : Action<StrategyPickerModel, StrategyPickerDependency>

fun interface StrategyPickerEffect : Effect<StrategyPickerModel, StrategyPickerDependency>

typealias StrategyPickerEmitter = ActionEmitter<StrategyPickerModel, StrategyPickerDependency>
