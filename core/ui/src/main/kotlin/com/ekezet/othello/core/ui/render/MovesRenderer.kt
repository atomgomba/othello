package com.ekezet.othello.core.ui.render

import androidx.compose.ui.graphics.ImageBitmap
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.PastMove
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.StateFlow

interface MovesRenderer {
    val renderedImages: StateFlow<ImmutableMap<String, ImageBitmap>>

    suspend fun updateJobs(moveHistory: MoveHistory): Any?
}

internal typealias OnRenderCallback = PastMove.() -> ImageBitmap
