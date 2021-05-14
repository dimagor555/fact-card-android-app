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
    fun setFileId(fileName: String) = viewModelScope.launch {
        fileCache.loadFileByName(fileName)
        fileCanvas.updateCanvas()
        updateFileLastUseTime(fileName)
    }

    private suspend fun updateFileLastUseTime(fileName: String) {
        val fileToUpdateLastUseTime = fileDao.getFileByName(fileName)
        fileToUpdateLastUseTime.lastUseTime = System.currentTimeMillis()
        fileDao.updateFile(fileToUpdateLastUseTime)
    }

    override fun onCleared() {
        CoroutineScope(Dispatchers.IO).launch { fileCache.synchronizeCacheWithDb() }
    }
}