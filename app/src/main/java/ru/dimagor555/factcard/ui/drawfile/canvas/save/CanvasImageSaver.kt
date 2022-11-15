package ru.dimagor555.factcard.ui.drawfile.canvas.save

import android.content.Context
import android.graphics.*
import android.widget.Toast
import androidx.core.graphics.toRect
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.creation.LinePathBuilder
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FileRenderer
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@ViewModelScoped
class CanvasImageSaver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileCache: FileCache,
    private val fileRenderer: FileRenderer,
    private val fileLayout: FileLayout,
    private val linePathBuilder: LinePathBuilder,
    private val imageWriter: ImageWriter,
) {
    fun saveFileToImage() {
        val imageBounds = computeImageBounds()
        val bitmap = Bitmap.createBitmap(
            imageBounds.width(),
            imageBounds.height(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        fileLayout.restore(-imageBounds.left.toFloat(), -imageBounds.top.toFloat(), 1F)
        fileRenderer.render(canvas)

        saveBitmapToGallery(bitmap)
    }

    private fun computeImageBounds(): Rect {
        var minX = Integer.MAX_VALUE
        var minY = Integer.MAX_VALUE
        var maxX = Integer.MIN_VALUE
        var maxY = Integer.MIN_VALUE

        val allBounds = computeCardsBounds() + computeLinesBounds()
        allBounds.forEach {
            if (it.left < minX) minX = it.left
            if (it.top < minY) minY = it.top
            if (it.right > maxX) maxX = it.right
            if (it.bottom > maxY) maxY = it.bottom
        }

        minX -= IMAGE_BORDERS_OFFSET
        minY -= IMAGE_BORDERS_OFFSET
        maxX += IMAGE_BORDERS_OFFSET
        maxY += IMAGE_BORDERS_OFFSET

        return Rect(minX, minY, maxX, maxY)
    }

    private fun computeCardsBounds(): List<Rect> {
        val toReturn = ArrayList<Rect>()
        fileCache.factCards.forEach {
            val cardLeft = it.positionX
            val cardRight = it.positionX + FactCardRenderModel.WIDTH
            val cardTop = it.positionY
            val cardBottom = it.positionY + FactCardRenderModel.HEIGHT

            toReturn += Rect(cardLeft, cardTop, cardRight, cardBottom)
        }
        return toReturn
    }

    private fun computeLinesBounds(): List<Rect> {
        val toReturn = ArrayList<Rect>()

        val linePath = Path()
        val lineBounds = RectF()
        fileCache.lines.forEach {
            linePathBuilder.buildLineToPath(it, linePath)
            linePath.computeBounds(lineBounds, false)
            toReturn += lineBounds.toRect()
        }
        return toReturn
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val fileName = generateFileName()
        if (imageWriter.hasPermissionToWriteFiles())
            if (imageWriter.writeFileToGallery(fileName, bitmap))
                showToastImageSaved()
            else
                showToastFailedToCreateFile()
        else
            showToastDoesNotHavePermissions()
    }

    private fun showToastFailedToCreateFile() = showToast(R.string.failed_to_create_file)

    private fun showToastDoesNotHavePermissions() =
        showToast(R.string.does_not_have_permissions_to_write_files)

    private fun showToastImageSaved() = showToast(R.string.image_saved_to_gallery)

    private fun showToast(stringResId: Int) {
        Toast.makeText(
            context,
            context.getText(stringResId),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun generateFileName(): String {
        val dateFormat = SimpleDateFormat("hhmmss_ddMMyyyy", Locale.ROOT)
        val currDate = dateFormat.format(Date(System.currentTimeMillis()))
        return "${fileCache.currFileId}_$currDate.png"
    }

    companion object {
        const val IMAGE_BORDERS_OFFSET = 50
    }
}