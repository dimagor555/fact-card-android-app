package ru.dimagor555.factcard.data.line

import androidx.room.*

@Dao
interface LineDao {
    @Query("select * from lines where fileName = :fileName")
    suspend fun getLinesByFileName(fileName: String): List<Line>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateLine(line: Line)

    @Delete
    suspend fun deleteLine(line: Line)
}