package ru.dimagor555.factcard.data.factcard

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fact_cards")
data class FactCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val fileId: Long,
    val positionX: Int,
    val positionY: Int,
)