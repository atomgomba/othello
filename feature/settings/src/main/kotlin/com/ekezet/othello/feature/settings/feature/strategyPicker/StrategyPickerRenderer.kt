package com.ekezet.othello.feature.settings.feature.strategyPicker

import com.ekezet.hurok.Renderer

class StrategyPickerRenderer : Renderer<StrategyPickerState, StrategyPickerModel> {
    override fun renderState(model: StrategyPickerModel) = with(model) {
        StrategyPickerState(pickForDisk = selectFor, selectedName = selected?.name, visible = visible)
    }
}
