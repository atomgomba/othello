package com.ekezet.othello.core.ui.di

import com.ekezet.othello.core.ui.render.DefaultMovesRenderer
import com.ekezet.othello.core.ui.render.MovesRenderer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers

@BindingContainer
object UiCoreBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun provideMovesRenderer(): MovesRenderer =
        DefaultMovesRenderer(
            coroutineContext = Dispatchers.IO,
        )
}
