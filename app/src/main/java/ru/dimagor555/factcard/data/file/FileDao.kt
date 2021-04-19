package ru.dimagor555.factcard.data.file

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("select * from files order by createTime desc")
    fun getAllFiles(): Flow<List<File>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFile(file: File)

    @Delete
    suspend fun deleteFile(file: File)
}