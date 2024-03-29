package ru.dimagor555.factcard.ui.drawfile.canvas

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FileLayout @Inject constructor() {
    var translateX = 0F
        private set
    var translateY = 0F
        private set
    var scale = DEFAULT_SCALE
        private set(value) {
            field = value.coerceIn(SCALE_RANGE)
        }

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

    fun reset() {
        translateX = 0F
        translateY = 0F
        scale = 1F
    }

    fun restore(x: Float, y: Float, scale: Float) {
        translateX = x
        translateY = y
        this.scale = scale
    }

    companion object {
        private const val DEFAULT_SCALE = 1f
        private val SCALE_RANGE = (DEFAULT_SCALE / 5f)..(DEFAULT_SCALE * 5f)
    }
}