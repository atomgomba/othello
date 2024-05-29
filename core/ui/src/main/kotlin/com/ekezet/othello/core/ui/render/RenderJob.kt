package com.ekezet.othello.core.ui.render

import android.graphics.Bitmap
import kotlinx.coroutines.Deferred

internal sealed interface RenderJob {
    data class Running(val deferred: Deferred<Unit>) : RenderJob
    data class Done(val renderId: String, val bitmap: Bitmap) : RenderJob
}
