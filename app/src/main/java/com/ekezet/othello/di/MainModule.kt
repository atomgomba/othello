package com.ekezet.othello.di

import com.ekezet.hurok.AnyLoopScope
import com.ekezet.othello.MainDependency
import com.ekezet.othello.feature.gameboard.di.GameBoardScopeName
import com.ekezet.othello.feature.gameboard.di.gameBoardFeatureModule
import com.ekezet.othello.mainLoop
import com.ekezet.othello.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal fun mainModule(parentScope: CoroutineScope, mainActivity: MainActivity) = module {
    includes(
        gameBoardFeatureModule(parentScope = parentScope),
    )

    single { mainActivity }

    single {
        MainDependency(
            mainActivity = get(),
            gameBoardScope = get(GameBoardScopeName),
        )
    }

    single<AnyLoopScope>(named("mainScope")) {
        mainLoop(
            parentScope = parentScope,
            dependency = get(),
        )
    }
}
