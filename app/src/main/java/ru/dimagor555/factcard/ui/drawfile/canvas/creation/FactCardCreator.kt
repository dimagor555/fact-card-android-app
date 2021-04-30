package ru.dimagor555.factcard.ui.drawfile.canvas.creation

import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.ui.drawfile.canvas.FileCanvas
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel
import javax.inject.Inject

@ViewModelScoped
class FactCardCreator @Inject constructor(
    private val fileCache: FileCache,
    private val fileLayout: FileLayout,
) {
    lateinit var fileCanvas: FileCanvas

    fun createFactCardInCenter() {
        val x = (fileCanvas.width / 2 - FactCardRenderModel.WIDTH / 2 * fileLayout.scale) / fileLayout.scale -
                fileLayout.translateX
        val y = (fileCanvas.height / 2 - FactCardRenderModel.HEIGHT / 2 * fileLayout.scale) / fileLayout.scale -
                fileLayout.translateY
        fileCache.onFactCardCreate(x.toInt(), y.toInt())
        fileCanvas.updateCanvas()
    }
}