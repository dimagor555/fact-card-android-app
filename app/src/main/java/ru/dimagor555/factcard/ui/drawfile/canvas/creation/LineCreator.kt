package ru.dimagor555.factcard.ui.drawfile.canvas.creation

import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.factcard.FactCard
import javax.inject.Inject

@ViewModelScoped
class LineCreator @Inject constructor(
    private val fileCache: FileCache,
) {
    fun createLine(firstCard: FactCard, secondCard: FactCard, secondCardPoint: Int) {
        val firstCardPoint = firstCard.selectedPoint
        firstCardPoint?.let {
            fileCache.onLineCreated(
                firstCard.id, secondCard.id,
                firstCardPoint, secondCardPoint
            )
        }
    }
}