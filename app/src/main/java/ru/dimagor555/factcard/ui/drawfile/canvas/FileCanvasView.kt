package ru.dimagor555.factcard.ui.drawfile.canvas

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.ScaleGestureDetector
import android.view.View
import ru.dimagor555.factcard.ui.drawfile.canvas.input.FileCanvasGestureListener

class FileCanvasView constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {
    var fileCanvas: FileCanvas? = null
        set(value) {
            field = value
            field!!.init(this)
            initGestureDetector(field!!.gestureListener)
        }

    private fun initGestureDetector(gestureListener: FileCanvasGestureListener) {
        val gestureDetector = GestureDetector(context, gestureListener)
        val scaleGestureDetector = ScaleGestureDetector(context, gestureListener)
        setOnTouchListener { _, event ->
            invalidate()
            scaleGestureDetector.onTouchEvent(event)
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        fileCanvas?.render(canvas)
    }
}