package ru.dimagor555.factcard.data.file

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {
    @Query("select * from files order by lastUseTime desc")
    fun getAllFiles(): Flow<List<File>>

    @Query("select count(*) from files where name = :name")
    suspend fun hasFileWithName(name: String): Boolean

    @Query("select * from files where name = :name")
    suspend fun getFileByName(name: String): File

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFile(file: File)

    @Delete
    suspend fun deleteFile(file: File)
}