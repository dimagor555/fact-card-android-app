package ru.dimagor555.factcard.ui.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.data.file.FileDao
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    private val fileDao: FileDao,
) : ViewModel() {
    val allFiles = fileDao.getAllFiles().asLiveData()

    fun deleteFile(file: File) = viewModelScope.launch {
        fileDao.deleteFile(file)
    }
}