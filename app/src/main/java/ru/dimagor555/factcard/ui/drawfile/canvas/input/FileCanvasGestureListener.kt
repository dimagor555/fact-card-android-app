package ru.dimagor555.factcard.ui.drawfile.canvas.input

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.ui.drawfile.canvas.CanvasMode
import ru.dimagor555.factcard.ui.drawfile.canvas.FileCanvas
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.input.factcard.FactCardInputProcessor
import javax.inject.Inject

@ViewModelScoped
class FileCanvasGestureListener @Inject constructor(
    private val fileLayout: FileLayout,
    private val factCardInputProcessor: FactCardInputProcessor,
    private val fileCanvasClickProcessor: FileCanvasClickProcessor,
) :
    GestureDetector.SimpleOnGestureListener(), ScaleGestureDetector.OnScaleGestureListener {
    var fileCanvas: FileCanvas? = null
        set(value) {
            field = value
            factCardInputProcessor.fileCanvas = value!!
            fileCanvasClickProcessor.fileCanvas = value
        }

    override fun onDown(e: MotionEvent) = true

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        if (isScaleEvent(e)) return false
        fileCanvasClickProcessor.processClick(e.x, e.y)
        return true
    }

    override fun onScroll(
        e1: MotionEvent?, e2: MotionEvent,
        distanceX: Float, distanceY: Float
    ): Boolean {
        if (isScaleEvent(e1) || isScaleEvent(e2)) return false

        val scaledDistanceX = distanceX / fileLayout.scale
        val scaledDistanceY = distanceY / fileLayout.scale

        when (fileCanvas?.mode?.value) {
            CanvasMode.CARD_SELECTED -> {
                factCardInputProcessor.onScroll(
                    fileCanvas?.selectedObject as FactCard,
                    scaledDistanceX,
                    scaledDistanceY
                )
            }
            CanvasMode.NOTHING_SELECTED, CanvasMode.LINE_CREATING -> {
                fileLayout.onScroll(
                    scaledDistanceX,
                    scaledDistanceY
                )
            }

            else -> Unit
        }

        return true
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        fileLayout.onScale(detector.scaleFactor)
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector) = true

    override fun onScaleEnd(detector: ScaleGestureDetector) {
    }

    private fun isScaleEvent(event: MotionEvent?) = event?.pointerCount == 2
}
