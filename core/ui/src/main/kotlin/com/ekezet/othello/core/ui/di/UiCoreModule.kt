package com.ekezet.othello.core.ui.di

import com.ekezet.othello.core.ui.render.DefaultMovesRenderer
import com.ekezet.othello.core.ui.render.MovesRenderer
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

fun uiCoreModule() = module {
    single<MovesRenderer> {
        DefaultMovesRenderer(
            coroutineContext = Dispatchers.IO,
        )
    }
}
