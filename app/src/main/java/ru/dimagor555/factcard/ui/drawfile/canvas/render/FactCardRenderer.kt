package ru.dimagor555.factcard.ui.drawfile.canvas.render

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.ui.drawfile.canvas.ColorManager
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import javax.inject.Inject

@ViewModelScoped
class FactCardRenderer @Inject constructor(
    private val fileLayout: FileLayout,
    private val fileCache: FileCache,
    private val textRenderer: TextRenderer,
    colorManager: ColorManager,
) {
    private val fillPaint = Paint()
    private val strokePaint = Paint()
    private val textPaint = TextPaint()
    private val pointFillPaint = Paint()
    private val selectedPointFillPaint = Paint()
    private val rect = RectF()

    init {
        fillPaint.color = colorManager.factCardBgColor
        fillPaint.style = Paint.Style.FILL
        strokePaint.color = colorManager.factCardBorderColor
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = FactCardRenderModel.BORDER_WIDTH
        textPaint.color = colorManager.factCardTextColor
        textPaint.textAlign = Paint.Align.CENTER

        pointFillPaint.color = colorManager.factCardPointColor
        pointFillPaint.style = Paint.Style.FILL_AND_STROKE
        selectedPointFillPaint.color = colorManager.factCardSelectedPointColor
        selectedPointFillPaint.style = Paint.Style.FILL_AND_STROKE
    }

    fun render(canvas: Canvas) {
        fileCache.factCards.forEach {
            renderCard(canvas, it)
        }
    }

    private fun renderCard(canvas: Canvas, card: FactCard) {
        drawCard(canvas, card)
        if (card.text.isNotEmpty())
            textRenderer.drawText(canvas, card)
        if (card.showPoints)
            drawPoints(canvas, card)
    }

    private fun drawCard(canvas: Canvas, card: FactCard) {
        val scale = fileLayout.scale
        val x = fileLayout.translateX + card.positionX
        val y = fileLayout.translateY + card.positionY
        rect.set(
            x * scale,
            y * scale,
            (x + FactCardRenderModel.WIDTH) * scale,
            (y + FactCardRenderModel.HEIGHT) * scale
        )

        val borderRadius = FactCardRenderModel.CARD_BORDER_RADIUS * scale
        canvas.drawRoundRect(rect, borderRadius, borderRadius, fillPaint)

        if (card.selected) {
            strokePaint.strokeWidth = FactCardRenderModel.BORDER_WIDTH * fileLayout.scale
            canvas.drawRoundRect(rect, borderRadius, borderRadius, strokePaint)
        }
    }

    private fun drawPoints(canvas: Canvas, card: FactCard) {
        val points = FactCardRenderModel.POINTS
        points.forEachIndexed { index, point ->
            val isPointSelected = index == card.selectedPoint
            val x = fileLayout.translateX + card.positionX + point.first
            val y = fileLayout.translateY + card.positionY + point.second
            drawPoint(canvas, x, y, isPointSelected)
        }
    }

    private fun drawPoint(canvas: Canvas, x: Float, y: Float, selected: Boolean) {
        val scale = fileLayout.scale
        val pointSize = FactCardRenderModel.POINT_SIZE.toFloat()
        rect.set(
            x * scale,
            y * scale,
            (x + pointSize) * scale,
            (y + pointSize) * scale
        )

        val radius = pointSize * scale
        val paint = if (selected) selectedPointFillPaint else pointFillPaint

        canvas.drawRoundRect(rect, radius, radius, paint)
    }
}
