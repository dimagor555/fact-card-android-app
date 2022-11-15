package ru.dimagor555.factcard.data.line

import androidx.room.*
import ru.dimagor555.factcard.data.file.File

@Entity(
    tableName = "lines",
    indices = [Index(
        value = [
                "fileId",
                "firstCardId",
                "secondCardId",
                "firstPointId",
                "secondPointId"
        ],
        unique = true
    )],
//    primaryKeys = [
//        "fileName",
//        "firstCardId",
//        "secondCardId",
//        "firstPointId",
//        "secondPointId"
//    ],
    foreignKeys = [ForeignKey(
        entity = File::class,
        parentColumns = ["idFile"],
        childColumns = ["fileId"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class Line(
    @PrimaryKey(autoGenerate = true) val idLine: Long = 0,
    val fileId: Long,
    val firstCardId: Long,
    val secondCardId: Long,
    val firstPointId: Int,
    val secondPointId: Int,
) {
    @Ignore
    var selected = false
}
