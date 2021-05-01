package ru.dimagor555.factcard.ui.drawfile.canvas.render

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.factcard.FactCard
import ru.dimagor555.factcard.ui.drawfile.canvas.ColorManager
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import javax.inject.Inject

@ViewModelScoped
class TextRenderer @Inject constructor(
    private val fileLayout: FileLayout,
    colorManager: ColorManager,
) {
    private val textPaint = TextPaint()

    init {
        textPaint.color = colorManager.factCardTextColor
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun drawText(canvas: Canvas, card: FactCard) {
        val scale = fileLayout.scale
        val textSize = card.textSize * scale
        val textMaxWidth = FactCardRenderModel.TEXT_WIDTH * scale
        textPaint.textSize = textSize

        val maxLines = (FactCardRenderModel.MAX_TEXT_HEIGHT / card.textSize).toInt()
        val textLines = computeTextLines(card.text, textMaxWidth, maxLines)
        val x = (fileLayout.translateX + card.positionX + FactCardRenderModel.WIDTH / 2) * scale

        drawTextLines(textLines, x, textSize, card, canvas)
    }

    private fun drawTextLines(
        textLines: List<String>,
        x: Float,
        textSize: Float,
        card: FactCard,
        canvas: Canvas,
    ) {
        val scale = fileLayout.scale
        val lines = textLines.size
        val linesHeight = lines * textSize
        val y = (fileLayout.translateY + card.positionY + FactCardRenderModel.HEIGHT / 2) *
                scale - linesHeight / 2 + textSize - textSize / 5

        for (i in 0 until lines) {
            canvas.drawText(textLines[i], x, y + textSize * i, textPaint)
        }
    }

    private fun computeTextLines(text: String, textMaxWidth: Float, maxLines: Int): List<String> {
        val lines = MutableList<String?>(0) { null }

        var textEnded = false
        var prevIndex = 0
        for (i in 0 until maxLines) {
            val measuredCharsCount = textPaint.breakText(
                text, prevIndex, text.length,
                true, textMaxWidth, null
            )
            val currIndex = prevIndex + measuredCharsCount
            lines += text.substring(prevIndex until currIndex)

            if (currIndex == text.length) {
                textEnded = true
                break
            }
            prevIndex = currIndex
        }
        if (!textEnded)
            lines[lines.lastIndex]?.let {
                lines[lines.lastIndex] = it.substring(0 until it.length - 3) + "..."
            }

        return lines.filterNotNull()
    }
}