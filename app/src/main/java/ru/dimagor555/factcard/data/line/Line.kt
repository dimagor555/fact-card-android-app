package ru.dimagor555.factcard.data.line

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import ru.dimagor555.factcard.data.file.File

@Entity(
    tableName = "lines",
    primaryKeys = [
        "fileName",
        "firstCardId",
        "secondCardId",
        "firstPointId",
        "secondPointId"
    ],
    foreignKeys = [ForeignKey(
        entity = File::class,
        parentColumns = ["name"],
        childColumns = ["fileName"],
        onDelete = ForeignKey.CASCADE,
    )]
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
