package com.ekezet.othello.core.ui.render

import android.graphics.Bitmap
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.ui.render.RenderJob.Done
import com.ekezet.othello.core.ui.render.RenderJob.Running
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HistoryImagesRenderer(
    jobDispatcher: CoroutineDispatcher,
) {
    private val _historyImages = MutableStateFlow<Map<String, Bitmap>>(emptyMap())
    val historyImages: StateFlow<Map<String, Bitmap>>
        get() = _historyImages.asStateFlow()

    private val renderJobs = mutableMapOf<String, RenderJob>()
    private val jobScope = CoroutineScope(jobDispatcher)
    private val mutex = Mutex()

    fun processHistory(moveHistory: MoveHistory) {
        jobScope.launch { processHistoryInner(moveHistory) }
    }

    private suspend fun processHistoryInner(moveHistory: MoveHistory) {
        val currentIds = moveHistory.map { it.renderId }.toSet()
        val oldIds = renderJobs.keys.toSet()
        val obsoleteIds = oldIds subtract currentIds
        for (obsoleteId in obsoleteIds) {
            val runningJob = renderJobs[obsoleteId] as? Running
            runningJob?.deferred?.cancel()
            removeJob(obsoleteId)
        }
        for (pastMove in moveHistory) {
            val currentId = pastMove.renderId
            if (!renderJobs.containsKey(currentId)) {
                createJob(pastMove)
            }
        }
    }

    private suspend fun createJob(pastMove: PastMove) {
        val renderId = pastMove.renderId
        mutex.withLock {
            renderJobs[renderId] = Running(
                jobScope.async { pastMove.doRendering(renderId) }
            )
        }
    }

    private suspend fun removeJob(renderId: String) {
        mutex.withLock {
            renderJobs.remove(renderId)
        }
        publishDoneImages()
    }

    private suspend fun finishJob(renderId: String, result: Bitmap) {
        mutex.withLock {
            renderJobs[renderId] = Done(renderId, result)
        }
        publishDoneImages()
    }

    private fun publishDoneImages() {
        val images = renderJobs.values
            .filterIsInstance<Done>()
            .associate { it.renderId to it.bitmap }
        _historyImages.value = images
    }

    private suspend fun PastMove.doRendering(renderId: String, drawBorder: Boolean = false) {
        finishJob(renderId, renderToBitmap(drawBorder))
    }
}
