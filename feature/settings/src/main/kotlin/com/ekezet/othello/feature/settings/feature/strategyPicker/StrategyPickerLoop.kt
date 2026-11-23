package com.ekezet.othello.feature.settings.feature.strategyPicker

import com.ekezet.hurok.DefaultEffectContext
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.hurok.Renderer
import kotlin.coroutines.CoroutineContext

class StrategyPickerLoop(
    model: StrategyPickerModel,
    renderer: Renderer<StrategyPickerState, StrategyPickerModel>,
    dependency: StrategyPickerDependency,
    effectContext: CoroutineContext = DefaultEffectContext
) : Loop<StrategyPickerState, StrategyPickerModel, Unit, StrategyPickerDependency, StrategyPickerAction>(
    model = model,
    renderer = renderer,
    dependency = dependency,
    effectContext = effectContext
) {
    internal companion object {
        operator fun invoke(dependency: StrategyPickerDependency) = LoopBuilder<StrategyPickerState, StrategyPickerModel, Unit, StrategyPickerDependency, StrategyPickerAction> {
            StrategyPickerLoop(
                model = StrategyPickerModel(),
                renderer = StrategyPickerRenderer(),
                dependency = dependency,
            )
        }
    }
}
