package com.ekezet.othello.feature.gameboard.di

import com.ekezet.hurok.AnyLoopScope
import com.ekezet.othello.feature.gameboard.defaultGameBoardArgs
import com.ekezet.othello.feature.gameboard.gameBoardLoop
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

fun gameBoardFeatureModule(parentScope: CoroutineScope) = module {
    single<AnyLoopScope>(GameBoardScopeName) {
        gameBoardLoop(parentScope = parentScope, args = defaultGameBoardArgs)
    }
}
