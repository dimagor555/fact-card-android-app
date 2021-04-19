package ru.dimagor555.factcard.data.line

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LineDao {
    @Query("select * from lines where fileId = :fileId")
    fun getLinesByFileId(fileId: Long): Flow<List<Line>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLine(line: Line)

    @Delete
    suspend fun deleteLine(line: Line)
}