package ru.dimagor555.factcard.ui.drawfile.canvas.input.factcard

import android.graphics.RectF
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel.POINT_SIZE
import javax.inject.Inject

@ViewModelScoped
class FactCardPointFinder @Inject constructor(
    private val fileLayout: FileLayout,
) {
    fun findClickedPoint(oldClickRect: RectF, cardRect: RectF): Int? {
        val points = FactCardRenderModel.POINTS

        val clickRect = computeClickRect(oldClickRect)

        val cardX = cardRect.left
        val cardY = cardRect.top
        val pointRect = RectF()

        points.forEachIndexed { index, point ->
            computePointRect(pointRect, cardX, cardY, point)

            if (RectF.intersects(clickRect, pointRect))
                return index
        }
        return null
    }

    private fun computePointRect(
        pointRect: RectF,
        cardX: Float,
        cardY: Float,
        point: Pair<Int, Int>
    ) {
        val scale = fileLayout.scale
        pointRect.set(
            cardX + point.first * scale,
            cardY + point.second * scale,
            cardX + (point.first + POINT_SIZE) * scale,
            cardY + (point.second + POINT_SIZE) * scale
        )
    }

    private fun computeClickRect(oldClickRect: RectF): RectF {
        val scale = fileLayout.scale

        val clickX = oldClickRect.centerX()
        val clickY = oldClickRect.centerY()
        return RectF(
            clickX - POINT_SIZE * scale,
            clickY - POINT_SIZE * scale,
            clickX + POINT_SIZE * scale,
            clickY + POINT_SIZE * scale
        )
    }
}