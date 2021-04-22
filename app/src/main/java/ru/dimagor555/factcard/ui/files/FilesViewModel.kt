package ru.dimagor555.factcard.ui.files

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.dimagor555.factcard.data.file.FileDao
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    fileDao: FileDao,
) : ViewModel() {
    val allFiles = fileDao.getAllFiles().asLiveData()
}