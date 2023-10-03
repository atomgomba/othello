package com.ekezet.othello

import com.ekezet.hurok.AnyLoopScope
import com.ekezet.hurok.Loop
import com.ekezet.hurok.LoopBuilder
import com.ekezet.othello.feature.gameboard.GameBoardScope
import com.ekezet.othello.feature.gamesettings.GameSettingsScope

internal class MainLoop private constructor(dependency: MainDependency) :
    Loop<MainState, MainModel, Unit, MainDependency, MainAction>(
        dependency = dependency,
    ) {

    override fun initModel() = MainModel()

    override fun renderState(model: MainModel) = with(model) {
        MainState(
            onNewGameClick = { emit(OnNewGameClicked) },
            onToggleIndicatorsClick = { emit(OnToggleIndicatorsClicked) },
            onShareGameClick = { emit(OnShareGameClicked) },
        )
    }

    @Suppress("UNCHECKED_CAST")
    override fun MainDependency.onAddChildLoop(child: AnyLoopScope) {
        gameBoardScope = child as? GameBoardScope
        gameSettingsScope = child as? GameSettingsScope
    }

    internal companion object Builder :
        LoopBuilder<MainState, MainModel, Unit, MainDependency, MainAction> {
        override fun invoke(
            args: Unit?,
            dependency: MainDependency?,
        ) = MainLoop(
            dependency = requireNotNull(dependency) { "MainLoop dependency must be set" },
        )
    }
}
