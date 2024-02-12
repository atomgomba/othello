package com.ekezet.othello.main

import com.ekezet.hurok.Renderer

internal class MainRenderer : Renderer<MainModel, MainDependency, MainState> {
    override fun renderState(model: MainModel) = MainState
}
