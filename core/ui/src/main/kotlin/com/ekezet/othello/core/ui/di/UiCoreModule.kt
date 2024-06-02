package com.ekezet.othello.core.ui.di

import com.ekezet.othello.core.ui.render.HistoryImagesRenderer
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

fun uiCoreModule() = module {
    single {
        HistoryImagesRenderer(
            jobDispatcher = Dispatchers.IO,
        )
    }
}
