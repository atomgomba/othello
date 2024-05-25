package com.ekezet.othello.feature.gamehistory.ui.viewModels

import androidx.compose.runtime.Immutable
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.data.models.Position
import com.ekezet.othello.core.ui.viewModels.HasComposeKey

@Immutable
internal data class HistoryItem(
    val turn: Int,
    val move: Position?,
    val disk: Disk,
) : HasComposeKey {
    override val composeKey: Any
        get() = "$turn-$move-$disk"
}
