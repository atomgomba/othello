package com.ekezet.othello.core.ui.render

import androidx.compose.ui.graphics.ImageBitmap
import com.ekezet.othello.core.game.MoveHistory
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.ui.render.DefaultMovesRenderer.RenderJob.Done
import com.ekezet.othello.core.ui.render.DefaultMovesRenderer.RenderJob.Running
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart.LAZY
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

internal class DefaultMovesRenderer(
    coroutineContext: CoroutineContext,
    private val onRender: OnRenderCallback,
) : MovesRenderer {

    constructor(coroutineContext: CoroutineContext) : this(
        coroutineContext = coroutineContext,
        onRender = PastMove::renderToBitmap,
    )

    private val _renderedImages = MutableStateFlow<ImmutableMap<String, ImageBitmap>>(persistentMapOf())
    override val renderedImages: StateFlow<ImmutableMap<String, ImageBitmap>>
        get() = _renderedImages.asStateFlow()

    private val renderJobs: MutableMap<String, RenderJob> = mutableMapOf()
    private val jobScope = CoroutineScope(SupervisorJob() + coroutineContext)
    private val mutex = Mutex()

    override suspend fun updateJobs(moveHistory: MoveHistory) = jobScope.launch {
        val newJobIds = moveHistory.map { move -> move.uuid }.toSet()
        val oldJobIds = mutex.withLock { renderJobs.keys - newJobIds }

        for (oldId in oldJobIds) {
            removeJob(oldId)
        }
        for (pastMove in moveHistory) {
            addJob(pastMove)
        }
    }

    private suspend fun removeJob(jobId: String) {
        mutex.withLock {
            val runningJob = renderJobs[jobId] as? Running
            runningJob?.deferred?.cancel()
            renderJobs.remove(jobId)
        }
        publishRenderedImages()
    }

    private suspend fun addJob(pastMove: PastMove) {
        val jobId = pastMove.uuid
        var deferred: Deferred<Unit>? = null
        mutex.withLock {
            if (renderJobs.containsKey(jobId)) {
                return
            }
            deferred = jobScope.async(start = LAZY) {
                finishJob(jobId = jobId, result = pastMove.onRender())
            }.also {
                renderJobs[jobId] = Running(deferred = it)
            }
        }
        deferred?.start()
    }

    private suspend fun finishJob(jobId: String, result: ImageBitmap) {
        var shouldPublish = false
        mutex.withLock {
            if (renderJobs[jobId] is Running) {
                renderJobs[jobId] = Done(jobId = jobId, bitmap = result)
                shouldPublish = true
            }
        }
        if (shouldPublish) {
            publishRenderedImages()
        }
    }

    private suspend fun publishRenderedImages() {
        val imagesToPublish = mutex.withLock {
            renderJobs.values
                .filterIsInstance<Done>()
                .associate { it.jobId to it.bitmap }
                .toImmutableMap()
        }
        _renderedImages.value = imagesToPublish
    }

    internal sealed interface RenderJob {
        data class Running(val deferred: Deferred<Unit>) : RenderJob
        data class Done(val jobId: String, val bitmap: ImageBitmap) : RenderJob
    }
}
