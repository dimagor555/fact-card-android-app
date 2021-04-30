package ru.dimagor555.factcard.ui.drawfile.canvas

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FileLayout @Inject constructor() {
    var translateX = 0F
        private set
    var translateY = 0F
        private set
    var scale = 1F
        private set

    val scaledTranslateX
        get() = translateX * scale
    val scaledTranslateY
        get() = translateY * scale

    fun onScroll(distanceX: Float, distanceY: Float) {
        translateX -= distanceX
        translateY -= distanceY
    }

    fun onScale(scaleFactor: Float) {
        scale *= scaleFactor
    }
}