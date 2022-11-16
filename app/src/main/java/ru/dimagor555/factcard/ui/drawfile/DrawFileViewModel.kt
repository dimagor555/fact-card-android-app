package ru.dimagor555.factcard.ui.drawfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dimagor555.factcard.data.FileCache
import ru.dimagor555.factcard.data.file.FileDao
import ru.dimagor555.factcard.ui.drawfile.canvas.FileCanvas
import javax.inject.Inject

@HiltViewModel
class DrawFileViewModel @Inject constructor(
    private val fileDao: FileDao,
    val fileCanvas: FileCanvas,
    private val fileCache: FileCache,
) : ViewModel() {
    fun setFileId(fileId: Long) = viewModelScope.launch {
        fileCache.loadFileById(fileId)
        fileCanvas.updateCanvas()
        updateFileLastUseTime(fileId)
    }

    private suspend fun updateFileLastUseTime(fileId: Long) {
        val fileToUpdateLastUseTime = fileDao.getFileById(fileId)
        fileToUpdateLastUseTime.lastUseTime = System.currentTimeMillis()
        fileDao.updateFile(fileToUpdateLastUseTime)
    }

    override fun onCleared() {
        CoroutineScope(Dispatchers.IO).launch { fileCache.synchronizeCacheWithDb() }
    }
}