package ru.dimagor555.factcard.ui.drawfile.canvas.input.factcard

import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.factcard.FactCard
import javax.inject.Inject
import kotlin.math.round

@ViewModelScoped
class FactCardMover @Inject constructor(
    private val fileCache: FileCache,
) {
    private var currCard: FactCard? = null
    private var accumulatorX = 0F
    private var accumulatorY = 0F

    fun moveCard(selectedCard: FactCard, distanceX: Float, distanceY: Float) {
        checkIfNewCard(selectedCard)
        accumulatorX -= distanceX
        accumulatorY -= distanceY

        val moveX = round(accumulatorX).toInt()
        val moveY = round(accumulatorY).toInt()
        updateMovementInCache(selectedCard, moveX, moveY)

        accumulatorX -= moveX
        accumulatorY -= moveY
    }

    private fun checkIfNewCard(selectedCard: FactCard) {
        if (currCard?.idFactCard != selectedCard.idFactCard)
            resetNewCard(selectedCard)
    }

    private fun resetNewCard(newCard: FactCard) {
        currCard = newCard
        accumulatorX = 0F
        accumulatorY = 0F
    }

    private fun updateMovementInCache(selectedCard: FactCard, moveX: Int, moveY: Int) {
        selectedCard.positionX += moveX
        selectedCard.positionY += moveY
        fileCache.onFactCardChanged(selectedCard)
    }
}