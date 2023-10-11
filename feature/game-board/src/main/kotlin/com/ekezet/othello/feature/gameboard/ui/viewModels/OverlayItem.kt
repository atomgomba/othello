package com.ekezet.othello.feature.gameboard.ui.viewModels

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.ekezet.othello.core.data.models.Disk
import com.ekezet.othello.core.ui.viewModels.HasComposeKey
import com.ekezet.othello.feature.gameboard.ui.components.overlay.NextMoveIndicator
import com.ekezet.othello.feature.gameboard.ui.components.overlay.ValidMoveIndicator
import java.util.Stack

internal sealed interface OverlayItem : HasComposeKey {
    @Composable
    fun Composable()

    data class StackedOverlayItem(
        private val items: Stack<OverlayItem>,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            items.forEach { it.Composable() }
        }

        override val composeKey: Any
            // FIXME: This is not always unique
            get() = "overlays-${items.size}"

        fun push(item: OverlayItem) = copy(items = items.apply { push(item) })
    }

    @Immutable
    data class ValidMoveIndicatorOverlayItem(
        private val disk: Disk,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            ValidMoveIndicator(disk)
        }

        override val composeKey: Any
            get() = "valid-move-indicator-${disk.isDark}"
    }

    data class NextMoveIndicatorOverlayItem(
        private val disk: Disk,
    ) : OverlayItem {
        @Composable
        override fun Composable() {
            NextMoveIndicator(disk)
        }

        override val composeKey: Any
            get() = "next-move-indicator"
    }
}
