package com.ekezet.othello.main

import com.ekezet.hurok.Renderer

internal class MainRenderer : Renderer<MainModel, MainState> {
    override fun renderState(model: MainModel) = MainState(
        hasGameHistory = model.hasGameHistory,
        isExitMessageVisible = model.isExitMessageVisible,
    )
}
