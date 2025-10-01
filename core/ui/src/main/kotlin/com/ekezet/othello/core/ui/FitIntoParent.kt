package com.ekezet.othello.core.ui

import androidx.annotation.FloatRange
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.node.LayoutModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastRoundToInt
import kotlin.math.min

/**
 * Constrain the size of a composable to make it fit into its parent.
 *
 * @param fraction The fraction of the maximum size to use, between `0` and `1`, inclusive.
 */
@Stable
fun Modifier.fitIntoParent(
    @FloatRange(from = 0.0, to = 1.0) fraction: Float = 1F,
) = this then FitIntoParentElement(fraction = fraction)

private class FitIntoParentElement(
    val fraction: Float,
) : ModifierNodeElement<FitIntoParentNode>() {
    init {
        require(fraction in 0F..1F) { "fraction must be between 0 and 1, but was $fraction" }
    }

    override fun create(): FitIntoParentNode = FitIntoParentNode(fraction = fraction)

    override fun update(node: FitIntoParentNode) {
        node.fraction = fraction
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "fitIntoParent"
        properties["fraction"] = fraction
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherElement = other as? FitIntoParentElement ?: return false
        return fraction == otherElement.fraction
    }

    override fun hashCode(): Int =
        fraction.hashCode()
}

private class FitIntoParentNode(
    var fraction: Float,
) : LayoutModifierNode, Modifier.Node() {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
    ): MeasureResult {
        val size = constraints.findSize()
        val wrappedConstraints = Constraints.fixed(size.width, size.height)
        val placeable = measurable.measure(wrappedConstraints)
        return layout(placeable.width, placeable.height) { placeable.placeRelative(0, 0) }
    }

    private fun Constraints.findSize(): IntSize {
        val minSize = min(maxWidth, maxHeight)
        val (width, height) = if (minSize == maxWidth) {
            minSize to minSize * (maxHeight / maxWidth)
        } else {
            minSize * (maxWidth / maxHeight) to minSize
        }
        return IntSize(
            width = (width * fraction).fastRoundToInt(),
            height = (height * fraction).fastRoundToInt(),
        )
    }
}
