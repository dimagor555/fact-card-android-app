package ru.dimagor555.factcard.ui.drawfile.canvas.render

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.ui.drawfile.canvas.ColorManager
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.creation.LinePathBuilder
import javax.inject.Inject

@ViewModelScoped
class LineRenderer @Inject constructor(
    private val fileLayout: FileLayout,
    private val fileCache: FileCache,
    private val linePathBuilder: LinePathBuilder,
    colorManager: ColorManager,
) {
    private val paint = Paint()
    private val path = Path()
    private val defaultLineColor = colorManager.lineColor
    private val selectedLineColor = colorManager.selectedLineColor

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
    }

    fun render(canvas: Canvas) =
        fileCache.lines.forEach { renderLine(canvas, it) }

    private fun renderLine(canvas: Canvas, line: Line) {
        linePathBuilder.buildLineToPath(line, path)

        paint.strokeWidth = LineRenderModel.LINE_WIDTH * fileLayout.scale
        paint.color = if (line.selected) selectedLineColor else defaultLineColor

        canvas.drawPath(path, paint)
    }
}