package com.ekezet.othello.feature.gameboard.di

import com.ekezet.othello.core.game.data.Default
import com.ekezet.othello.core.game.data.GameSettings
import com.ekezet.othello.feature.gameboard.GameBoardEmitter
import com.ekezet.othello.feature.gameboard.GameBoardLoop
import org.koin.dsl.module

fun gameBoardFeatureModule() = module {
    single { GameBoardLoop.build(GameSettings.Default) }

    single<GameBoardEmitter> { get<GameBoardLoop>() }
}
