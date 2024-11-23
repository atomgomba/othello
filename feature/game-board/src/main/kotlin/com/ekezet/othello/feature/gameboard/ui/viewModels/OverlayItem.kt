package com.ekezet.othello.feature.gameboard.ui.viewModels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.viewModels.Sprite
import com.ekezet.othello.feature.gameboard.ui.components.overlay.NextMoveIndicator
import com.ekezet.othello.feature.gameboard.ui.components.overlay.PastMoveIndicator
import com.ekezet.othello.feature.gameboard.ui.components.overlay.ValidMoveIndicator
import java.util.Stack

@Stable
internal sealed interface OverlayItem : Sprite {
    @Stable
    data class StackedOverlayItem(
        private val items: Stack<OverlayItem>,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            items.forEach { it.Composable() }
        }

        fun push(item: OverlayItem) = copy(items = items.apply { push(item) })
    }

    @Stable
    data class ValidMoveIndicatorOverlayItem(
        private val disk: Disk,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            ValidMoveIndicator(disk)
        }
    }

    @Stable
    data class NextMoveIndicatorOverlayItem(
        private val disk: Disk,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            NextMoveIndicator(disk)
        }
    }

    @Stable
    data class PastMoveIndicatorOverlayItem(
        private val disk: Disk,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            PastMoveIndicator(disk)
        }
    }
}
