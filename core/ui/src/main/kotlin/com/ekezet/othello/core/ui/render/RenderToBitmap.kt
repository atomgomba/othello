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

fun PastMove.renderToBitmap(drawBorder: Boolean = false): Bitmap {
    val (width, height) = listOf(640F, 640F)
    val (cellWidth, cellHeight) = listOf(width / BoardWidth, height / BoardHeight)
    val cellPadding = cellWidth * 0.1F
    val gamePieceSize = cellWidth - (2 * cellPadding)
    val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
    var x: Float
    var y = cellPadding
    val paint = Paint()
    Canvas(bitmap).apply {
        for ((rowIndex, row) in board.withIndex()) {
            x = cellPadding
            if (drawBorder) {
                paint.style = Paint.Style.STROKE
                paint.color = Color.White.toArgb()
                paint.strokeWidth = cellPadding / 4
                drawLine(0F, rowIndex * cellHeight, width, rowIndex * cellHeight, paint)
            }
            for ((colIndex, disk) in row.withIndex()) {
                if (drawBorder) {
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.White.toArgb()
                    drawLine(colIndex * cellWidth, 0F, colIndex * cellWidth, height, paint)
                }
                if (disk != null) {
                    paint.style = Paint.Style.FILL
                    paint.color = disk.color.toArgb()
                    drawCircle(
                        x + gamePieceSize / 2,
                        y + gamePieceSize / 2,
                        gamePieceSize / 2F,
                        paint
                    )
                    val move = moveAt
                    if (move != null && colIndex == move.x && rowIndex == move.y) {
                        paint.color = Color(255, 98, 0).toArgb()
                        paint.style = Paint.Style.STROKE
                        paint.strokeWidth = gamePieceSize / 4F
                        drawCircle(
                            x + gamePieceSize / 2,
                            y + gamePieceSize / 2,
                            gamePieceSize / 2F,
                            paint
                        )
                    }
                }
                paint.reset()
                x += cellWidth
            }
            y += cellHeight
        }
    }
    return bitmap
}
