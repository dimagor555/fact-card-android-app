package ru.dimagor555.factcard.ui.drawfile.canvas.save

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class ImageWriter @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun hasPermissionToWriteFiles(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun writeFileToGallery(fileName: String, bitmap: Bitmap): Boolean {
        val picturesUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, fileName)
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.DESCRIPTION, "")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            }
        }

        val resolver = context.contentResolver
        var imageUri: Uri? = null

        try {
            imageUri = resolver.insert(picturesUri, imageDetails)
                ?: throw IOException("Failed to insert image")

            val outStream = resolver.openOutputStream(imageUri)
                ?: throw IOException("Failed to open output stream")

            outStream.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            }
            return true
        } catch (e: Exception) {
            if (imageUri != null)
                resolver.delete(imageUri, null, null)
            return false
        }
    }
}

