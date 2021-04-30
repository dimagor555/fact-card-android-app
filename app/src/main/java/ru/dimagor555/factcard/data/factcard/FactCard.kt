package ru.dimagor555.factcard.data.factcard

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import ru.dimagor555.factcard.ui.drawfile.canvas.render.FactCardRenderModel

@Entity(tableName = "fact_cards")
data class FactCard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var text: String = "",
    val fileName: String,
    var positionX: Int = 0,
    var positionY: Int = 0,
    @ColumnInfo(name = "textSize")
    private var _textSize: Int = FactCardRenderModel.MAX_TEXT_HEIGHT.toInt() / 2,
) {
    val textSize
        get() = _textSize

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
        if (_textSize + TEXT_SIZE_INTERVAL <= MAX_TEXT_SIZE)
            _textSize += TEXT_SIZE_INTERVAL
    }

    fun decreaseTextSize() {
        if (_textSize - TEXT_SIZE_INTERVAL >= MIN_TEXT_SIZE)
            _textSize -= TEXT_SIZE_INTERVAL
    }

    companion object {
        const val MIN_TEXT_SIZE = 10
        const val MAX_TEXT_SIZE = FactCardRenderModel.MAX_TEXT_HEIGHT
        const val TEXT_SIZE_INTERVAL = 5
    }
}

