package ru.dimagor555.factcard.data.factcard

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FactCardDao {
    @Query("select * from fact_cards where fileId = :fileId")
    fun getFactCardsByFileId(fileId: Long): Flow<List<FactCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFactCard(factCard: FactCard)

    @Delete
    suspend fun deleteFactCard(factCard: FactCard)
}