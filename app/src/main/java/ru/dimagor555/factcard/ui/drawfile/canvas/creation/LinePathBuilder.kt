package ru.dimagor555.factcard.ui.drawfile.canvas.creation

import android.graphics.Path
import android.graphics.Rect
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel.POINT_SIZE
import ru.dimagor555.factcard.ui.drawfile.canvas.render.LineRenderModel
import javax.inject.Inject

@ViewModelScoped
class LinePathBuilder @Inject constructor(
    private val fileCache: FileCache,
    private val fileLayout: FileLayout,
) {
    fun buildLineToPath(line: Line, path: Path) {
        path.rewind()
        val startPoint = getPointOfCardById(line.firstCardId, line.firstPointId)
        val endPoint = getPointOfCardById(line.secondCardId, line.secondPointId)

        val middlePoints = computeMiddlePoints(
            startPoint, endPoint,
            line.firstPointId, line.secondPointId
        )
        val middlePoint1 = middlePoints.first
        val middlePoint2 = middlePoints.second

        path.moveTo(startPoint.first, startPoint.second)
        path.cubicTo(
            middlePoint1.first, middlePoint1.second,
            middlePoint2.first, middlePoint2.second,
            endPoint.first, endPoint.second
        )
    }

    private fun computeMiddlePoints(
        startPoint: Pair<Float, Float>,
        endPoint: Pair<Float, Float>,
        firstPointId: Int,
        secondPointId: Int
    ): Pair<Pair<Float, Float>, Pair<Float, Float>> {
        val shift = LineRenderModel.BEZIER_SHIFT * fileLayout.scale

        val firstMiddlePoint =
            computeMiddlePoint(startPoint, shift, firstPointId)
        val secondMiddlePoint =
            computeMiddlePoint(endPoint, shift, secondPointId)

        return firstMiddlePoint to secondMiddlePoint
    }

    private fun computeMiddlePoint(
        point: Pair<Float, Float>,
        shift: Float,
        pointId: Int
    ): Pair<Float, Float> {
        val shiftX: Float = when (pointId) {
            1, 2, 3, 7, 8, 9 -> 0F
            0, 10, 11 -> -shift
            4, 5, 6 -> shift
            else -> 0F
        }
        val shiftY: Float = when (pointId) {
            5, 11 -> 0F
            0, 1, 2, 3, 4 -> -shift
            6, 7, 8, 9, 10 -> shift
            else -> 0F
        }
        return point.first + shiftX to point.second + shiftY
    }

    private fun getPointOfCardById(cardId: Long, pointId: Int): Pair<Float, Float> {
        val card = fileCache.factCards.find { it.idFactCard == cardId }!!
        val pointInCard = FactCardRenderModel.POINTS[pointId]
        val point = Rect(
            pointInCard.first, pointInCard.second,
            pointInCard.first + POINT_SIZE, pointInCard.second + POINT_SIZE
        )

        val x = (card.positionX + fileLayout.translateX + point.exactCenterX()) * fileLayout.scale
        val y = (card.positionY + fileLayout.translateY + point.exactCenterY()) * fileLayout.scale

        return x to y
    }
}