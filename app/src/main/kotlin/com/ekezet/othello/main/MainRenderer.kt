package com.ekezet.othello.main

import com.ekezet.hurok.Renderer

internal class MainRenderer : Renderer<MainModel, MainDependency, MainState> {
    override fun renderState(model: MainModel) = MainState(
        hasGameHistory = model.hasGameHistory,
        isExitMessageVisible = model.isExitMessageVisible,
    )
}

internal val MainModel.isExitMessageVisible: Boolean
    get() = 0 < backPressCount
