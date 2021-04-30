package ru.dimagor555.factcard.ui.drawfile.canvas.input

import android.graphics.Path
import android.graphics.RectF
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.ui.drawfile.canvas.CanvasMode
import ru.dimagor555.factcard.ui.drawfile.canvas.FileCanvas
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.creation.LinePathBuilder
import ru.dimagor555.factcard.ui.drawfile.canvas.input.factcard.FactCardInputProcessor
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel
import javax.inject.Inject

@ViewModelScoped
class FileCanvasClickProcessor @Inject constructor(
    private val fileCache: FileCache,
    private val fileLayout: FileLayout,
    private val factCardInputProcessor: FactCardInputProcessor,
    private val linePathBuilder: LinePathBuilder,
) {
    lateinit var fileCanvas: FileCanvas

    fun processClick(x: Float?, y: Float?) {
        if (x == null || y == null) return

        val clickedObj = findClickedObj(x, y)
        if(fileCanvas.mode.value != CanvasMode.LINE_CREATING || clickedObj == null) {
            fileCanvas.selectObject(clickedObj)
        }
    }

    private fun findClickedObj(x: Float, y: Float): Any? {
        val clickX = x - fileLayout.scaledTranslateX
        val clickY = y - fileLayout.scaledTranslateY
        val clickRect = RectF(clickX - 1, clickY - 1, clickX + 1, clickY + 1)

        findClickedFactCard(clickRect)?.let {
            return it
        }
        findClickedLine(clickRect)?.let {
            return it
        }

        return null
    }

    private fun findClickedFactCard(clickRect: RectF): FactCard? {
        val cardRect = RectF()
        fileCache.factCards.forEach {
            val x = it.positionX.toFloat()
            val y = it.positionY.toFloat()
            cardRect.set(
                x * fileLayout.scale,
                y * fileLayout.scale,
                (x + FactCardRenderModel.WIDTH) * fileLayout.scale,
                (y + FactCardRenderModel.HEIGHT) * fileLayout.scale
            )
            if (RectF.intersects(cardRect, clickRect)) {
                factCardInputProcessor.onClick(it, clickRect, cardRect)
                return it
            }
        }
        return null
    }

    private fun findClickedLine(clickRect: RectF): Line? {
        val linePath = Path()
        val lineBounds = RectF()
        fileCache.lines.forEach {
            linePathBuilder.buildLineToPath(it, linePath)
            linePath.computeBounds(lineBounds, false)

            lineBounds.left -= fileLayout.scaledTranslateX
            lineBounds.right -= fileLayout.scaledTranslateX
            lineBounds.top -= fileLayout.scaledTranslateY
            lineBounds.bottom -= fileLayout.scaledTranslateY

            if (RectF.intersects(lineBounds, clickRect)) {
                return it
            }
        }
        return null
    }
}