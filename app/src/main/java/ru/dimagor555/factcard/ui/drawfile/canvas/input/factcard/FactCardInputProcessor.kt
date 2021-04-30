package ru.dimagor555.factcard.ui.drawfile.canvas.input.factcard

import android.graphics.RectF
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.ui.drawfile.canvas.CanvasMode
import ru.dimagor555.factcard.ui.drawfile.canvas.FileCanvas
import ru.dimagor555.factcard.ui.drawfile.canvas.creation.LineCreator
import javax.inject.Inject

@ViewModelScoped
class FactCardInputProcessor @Inject constructor(
    private val factCardMover: FactCardMover,
    private val factCardPointFinder: FactCardPointFinder,
    private val lineCreator: LineCreator,
) {
    lateinit var fileCanvas: FileCanvas

    fun onScroll(selectedCard: FactCard, distanceX: Float, distanceY: Float) =
        factCardMover.moveCard(selectedCard, distanceX, distanceY)

    fun onClick(card: FactCard, oldClickRect: RectF, cardRect: RectF) {
        val clickedPoint = factCardPointFinder.findClickedPoint(oldClickRect, cardRect)
        if (card.showPoints && card == fileCanvas.selectedObject && clickedPoint != null) {
            card.selectedPoint = clickedPoint
            fileCanvas.onStartLineCreating()
        } else if (fileCanvas.mode.value == CanvasMode.LINE_CREATING) {
            clickedPoint?.let {
                lineCreator.createLine(
                    fileCanvas.selectedObject as FactCard,
                    card, clickedPoint
                )
                fileCanvas.onFinishLineCreating()
            }
        }
    }
}