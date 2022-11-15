package ru.dimagor555.factcard.ui.createfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.data.file.FileDao
import javax.inject.Inject

@HiltViewModel
class CreateFileViewModel @Inject constructor(
    private val fileDao: FileDao,
) : ViewModel() {
    private val _fileId = MutableLiveData<Long?>(null)
//    private val _fileName = MutableLiveData<String?>(null)
    private val _error = MutableLiveData<Int?>(null)
    private var createFileInProgress = false

    val fileId: LiveData<Long?> = _fileId
//    val fileName: LiveData<String?> = _fileName
    val error: LiveData<Int?> = _error

    fun onClickCreate(name: String) {
        if (createFileInProgress) return
        viewModelScope.launch {
            when {
                name.isBlank() -> _error.value = R.string.file_name_empty_error
                fileDao.hasFileWithName(name) -> _error.value = R.string.file_already_exists_error
                else -> {
                    createFileInProgress = true
                    val fileToCreate = File(
                        name = name,
                        lastUseTime = System.currentTimeMillis()
                    )
                    createFile(fileToCreate)
                }
            }
        }
    }

    private fun createFile(file: File) {
        viewModelScope.launch {
            Log.d("CREATE FILE", file.toString())
            file.idFile = fileDao.insertFile(file)
            Log.d("CREATE FILE 2", file.toString())
            _fileId.postValue(file.idFile)
//            _fileName.postValue(file.name)
        }
    }
}