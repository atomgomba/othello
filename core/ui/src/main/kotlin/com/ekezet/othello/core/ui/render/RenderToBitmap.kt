package com.ekezet.othello.core.ui.render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.ekezet.othello.core.data.models.BoardHeight
import com.ekezet.othello.core.data.models.BoardWidth
import com.ekezet.othello.core.data.models.x
import com.ekezet.othello.core.data.models.y
import com.ekezet.othello.core.game.PastMove
import com.ekezet.othello.core.ui.components.color

private const val bitmapWidth = 640F
private const val bitmapHeight = 640F
private const val cellWidth = bitmapWidth / BoardWidth
private const val cellHeight = bitmapHeight / BoardHeight
private const val cellPadding = cellWidth * 0.1F
private const val gamePieceSize = cellWidth - (2F * cellPadding)
private const val diskRadius = gamePieceSize / 2F

private val borderPaint = Paint().apply {
    style = Paint.Style.STROKE
    color = Color.White.toArgb()
    strokeWidth = cellPadding / 4
}

private val diskPaint = Paint().apply {
    style = Paint.Style.FILL
}

private val movePaint = Paint().apply {
    color = Color(255, 98, 0).toArgb()
    style = Paint.Style.STROKE
    strokeWidth = gamePieceSize / 4
}

fun PastMove.renderToBitmap(drawBorder: Boolean = false): Bitmap {
    val bitmap = Bitmap.createBitmap(bitmapWidth.toInt(), bitmapHeight.toInt(), Bitmap.Config.ARGB_8888)
    var x: Float
    var y = cellPadding
    Canvas(bitmap).apply {
        for ((rowIndex, row) in board.withIndex()) {
            x = cellPadding
            if (drawBorder) {
                drawLine(0F, rowIndex * cellHeight, bitmapWidth, rowIndex * cellHeight, borderPaint)
            }
            for ((colIndex, disk) in row.withIndex()) {
                if (drawBorder) {
                    drawLine(colIndex * cellWidth, 0F, colIndex * cellWidth, bitmapHeight, borderPaint)
                }
                if (disk != null) {
                    diskPaint.color = disk.color.toArgb()
                    drawCircle(x + diskRadius, y + diskRadius, diskRadius, diskPaint)
                    val move = moveAt
                    if (move != null && colIndex == move.x && rowIndex == move.y) {
                        drawCircle(x + diskRadius, y + diskRadius, diskRadius, movePaint)
                    }
                }
                x += cellWidth
            }
            y += cellHeight
        }
    }
    return bitmap
}
