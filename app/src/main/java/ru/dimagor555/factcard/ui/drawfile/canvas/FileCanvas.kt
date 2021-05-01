package ru.dimagor555.factcard.ui.drawfile.canvas

import android.graphics.Canvas
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.data.line.Line
import ru.dimagor555.factcard.ui.drawfile.canvas.creation.FactCardCreator
import ru.dimagor555.factcard.ui.drawfile.canvas.input.FileCanvasGestureListener
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FileRenderer
import ru.dimagor555.factcard.ui.drawfile.canvas.save.CanvasImageSaver
import javax.inject.Inject

@ViewModelScoped
class FileCanvas @Inject constructor(
    private val fileCache: FileCache,
    private val fileRenderer: FileRenderer,
    private val fileLayout: FileLayout,
    val gestureListener: FileCanvasGestureListener,
    private val factCardCreator: FactCardCreator,
    private val canvasImageSaver: CanvasImageSaver,
) {
    private lateinit var canvasView: FileCanvasView
    var width = 0
    var height = 0

    private val _mode = MutableLiveData(CanvasMode.NOTHING_SELECTED)
    val mode: LiveData<CanvasMode> = _mode

    var selectedObject: Any? = null
        private set

    init {
        gestureListener.fileCanvas = this
        factCardCreator.fileCanvas = this
    }

    fun init(fileCanvasView: FileCanvasView) {
        canvasView = fileCanvasView
    }

    fun selectObject(obj: Any?) {
        selectedObject = obj
        fileCache.factCards.filter { it != selectedObject }
            .forEach { it.selected = false }
        fileCache.lines.filter { it != selectedObject }
            .forEach { it.selected = false }

        when (obj) {
            null -> _mode.value = CanvasMode.NOTHING_SELECTED
            is FactCard -> {
                _mode.value = CanvasMode.CARD_SELECTED
                obj.selected = true
            }
            is Line -> {
                _mode.value = CanvasMode.LINE_SELECTED
                obj.selected = true
            }
        }

        updateCanvas()
    }

    fun onStartLineCreating() {
        _mode.value = CanvasMode.LINE_CREATING
        fileCache.factCards.forEach { it.showPoints = true }
        updateCanvas()
    }

    fun onFinishLineCreating() = selectObject(null)

    fun createFactCardInCenter() = factCardCreator.createFactCardInCenter()

    fun deleteSelectedFactCard() {
        if (factCardSelected) {
            fileCache.onFactCardDeleted(selectedObject as FactCard)
            selectObject(null)
        }
    }

    fun increaseSelectedFactCardTextSize() {
        if (factCardSelected) {
            val selectedCard = selectedObject as FactCard
            selectedCard.increaseTextSize()
            fileCache.onFactCardChanged(selectedCard)
            updateCanvas()
        }
    }

    fun decreaseSelectedFactCardTextSize() {
        if (factCardSelected) {
            val selectedCard = selectedObject as FactCard
            selectedCard.decreaseTextSize()
            fileCache.onFactCardChanged(selectedCard)
            updateCanvas()
        }
    }

    private val factCardSelected
        get() = _mode.value == CanvasMode.CARD_SELECTED && selectedObject is FactCard

    fun startEditingCardText() {
        _mode.value = CanvasMode.CARD_TEXT_EDITING
    }

    fun finishEditingText(text: String?) {
        selectedCardText = text
        _mode.value = CanvasMode.CARD_SELECTED
    }

    var selectedCardText: String?
        get() =
            if (selectedObject is FactCard) (selectedObject as FactCard).text
            else null
        private set(value) {
            if (_mode.value == CanvasMode.CARD_TEXT_EDITING &&
                selectedObject is FactCard && value != null
            ) {
                val selectedCard = selectedObject as FactCard
                selectedCard.text = value
                fileCache.onFactCardChanged(selectedCard)
                updateCanvas()
            }
        }

    fun deleteSelectedLine() {
        if (lineSelected) {
            fileCache.onLineDeleted(selectedObject as Line)
            selectObject(null)
        }
    }

    private val lineSelected
        get() = _mode.value == CanvasMode.LINE_SELECTED && selectedObject is Line

    fun saveCanvasImageToGallery() {
        selectObject(null)
        val oldTranslateX = fileLayout.translateX
        val oldTranslateY = fileLayout.translateY
        val oldScale = fileLayout.scale
        fileLayout.reset()

        canvasImageSaver.saveFileToImage()

        fileLayout.restore(oldTranslateX, oldTranslateY, oldScale)
    }

    fun render(canvas: Canvas) {
        width = canvas.width
        height = canvas.height
        fileRenderer.render(canvas)
    }

    fun updateCanvas() {
        canvasView.invalidate()
    }
}
