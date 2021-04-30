package ru.dimagor555.factcard.data.factcard

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FactCardDao {
    @Query("select * from fact_cards where fileName = :fileName")
    suspend fun getFactCardsByFileName(fileName: String): List<FactCard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateFactCard(factCard: FactCard)

    @Query("delete from fact_cards where id = :id")
    suspend fun deleteFactCardById(id: Long)
}