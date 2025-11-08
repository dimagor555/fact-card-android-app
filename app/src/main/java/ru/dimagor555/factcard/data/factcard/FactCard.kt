package ru.dimagor555.factcard.data.factcard

import androidx.room.*
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel

@Entity(
    tableName = "fact_cards",
    foreignKeys = [ForeignKey(
        entity = File::class,
        parentColumns = ["idFile"],
        childColumns = ["fileId"],
        onDelete = ForeignKey.CASCADE,
    )]
)
data class FactCard(
    @PrimaryKey(autoGenerate = true) val idFactCard: Long = 0,
    var text: String = "",
    val fileId: Long,
    var positionX: Int = 0,
    var positionY: Int = 0,
    @ColumnInfo(name = "textSize")
    var textSize: Int = FactCardRenderModel.MAX_TEXT_HEIGHT.toInt() / 2,
) {

    @Ignore
    var selected = false
        set(value) {
            field = value
            showPoints = value
        }

    @Ignore
    var showPoints = false
        set(value) {
            field = value
            if (!value)
                selectedPoint = null
        }

    @Ignore
    var selectedPoint: Int? = null

    fun increaseTextSize() {
        if (textSize + TEXT_SIZE_INTERVAL <= MAX_TEXT_SIZE)
            textSize += TEXT_SIZE_INTERVAL
    }

    fun decreaseTextSize() {
        if (textSize - TEXT_SIZE_INTERVAL >= MIN_TEXT_SIZE)
            textSize -= TEXT_SIZE_INTERVAL
    }

    companion object {
        const val MIN_TEXT_SIZE = 10
        const val MAX_TEXT_SIZE = FactCardRenderModel.MAX_TEXT_HEIGHT
        const val TEXT_SIZE_INTERVAL = 5
    }
}

