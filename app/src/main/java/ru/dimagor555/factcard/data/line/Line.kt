package ru.dimagor555.factcard.data.line

import androidx.room.Entity

@Entity(
    tableName = "lines",
    primaryKeys = [
        "fileId",
        "firstCardId",
        "secondCardId",
        "firstPointId",
        "secondPointId"
    ]
)
data class Line(
    val fileId: Long,
    val firstCardId: Long,
    val secondCardId: Long,
    val firstPointId: Int,
    val secondPointId: Int,
)
