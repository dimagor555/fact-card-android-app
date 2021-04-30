package ru.dimagor555.factcard.data.line

import androidx.room.Entity
import androidx.room.Ignore

@Entity(
    tableName = "lines",
    primaryKeys = [
        "fileName",
        "firstCardId",
        "secondCardId",
        "firstPointId",
        "secondPointId"
    ]
)
data class Line(
    val fileName: String,
    val firstCardId: Long,
    val secondCardId: Long,
    val firstPointId: Int,
    val secondPointId: Int,
) {
    @Ignore
    var selected = false
}
