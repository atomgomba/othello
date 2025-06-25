package com.ekezet.othello.core.ui.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.ui.color

private const val bitmapWidth = 640F
private const val bitmapHeight = bitmapWidth
private const val cellWidth = bitmapWidth / BoardWidth
private const val cellHeight = bitmapHeight / BoardHeight
private const val cellPadding = cellWidth * 0.1F
private const val gamePieceSize = cellWidth - (2F * cellPadding)
private const val diskRadius = gamePieceSize / 2F

private val borderPaint by lazy {
    Paint().apply {
        style = PaintingStyle.Stroke
        color = Color.White
        strokeWidth = cellPadding / 4
    }
}

private val diskPaint by lazy {
    Paint().apply {
        style = PaintingStyle.Fill
    }
}

val moveHighlightColor = Color(255, 98, 0)

private val movePaint by lazy {
    Paint().apply {
        style = PaintingStyle.Stroke
        color = moveHighlightColor
        strokeWidth = gamePieceSize / 4
    }
}

fun PastMove.renderToBitmap(drawBorder: Boolean = false): ImageBitmap {
    val bitmap = ImageBitmap(bitmapWidth.toInt(), bitmapHeight.toInt())
    var offset = Offset(cellPadding, cellPadding)
    Canvas(bitmap).apply {
        for ((rowIndex, row) in board.withIndex()) {
            offset = Offset(cellPadding, offset.y)
            if (drawBorder) {
                drawLine(Offset(0F, rowIndex * cellHeight), Offset(bitmapWidth, rowIndex * cellHeight), borderPaint)
            }
            for ((colIndex, disk) in row.withIndex()) {
                if (drawBorder) {
                    drawLine(Offset(colIndex * cellWidth, 0F), Offset(colIndex * cellWidth, bitmapHeight), borderPaint)
                }
                if (disk != null) {
                    diskPaint.color = disk.color
                    drawCircle(Offset(offset.x + diskRadius, offset.y + diskRadius), diskRadius, diskPaint)
                    moveAt?.let { move ->
                        if (colIndex == move.x && rowIndex == move.y) {
                            drawCircle(Offset(offset.x + diskRadius, offset.y + diskRadius), diskRadius, movePaint)
                        }
                    }
                }
                offset += Offset(cellWidth, 0F)
            }
            offset += Offset(0F, cellHeight)
        }
    }
    return bitmap
}
