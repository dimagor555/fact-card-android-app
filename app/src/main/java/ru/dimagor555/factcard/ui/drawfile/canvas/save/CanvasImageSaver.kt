package ru.dimagor555.factcard.ui.drawfile.canvas.save

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.ui.drawfile.canvas.FileLayout
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FileRenderer
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@ViewModelScoped
class CanvasImageSaver @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fileCache: FileCache,
    private val fileRenderer: FileRenderer,
    private val fileLayout: FileLayout,
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
        fileCache.factCards.forEach {
            val cardX = it.positionX
            val cardY = it.positionY

            if (cardX < minX) minX = cardX
            if (cardY < minY) minY = cardY

            if (cardX > maxX) maxX = cardX
            if (cardY > maxY) maxY = cardY
        }
        maxX += FactCardRenderModel.WIDTH
        maxY += FactCardRenderModel.HEIGHT

        minX -= IMAGE_BORDERS_OFFSET
        minY -= IMAGE_BORDERS_OFFSET
        maxX += IMAGE_BORDERS_OFFSET
        maxY += IMAGE_BORDERS_OFFSET

        return Rect(minX, minY, maxX, maxY)
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
        return "${fileCache.currFileName}_$currDate.png"
    }

    companion object {
        const val IMAGE_BORDERS_OFFSET = 50
    }
}