package ru.dimagor555.factcard.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.ui.drawfile.canvas.ColorManager

object SettingsResetter {
    fun resetAllColors(sharedPreferences: SharedPreferences, context: Context) {
        val edit = sharedPreferences.edit()
        edit.putInt(ColorManager.COLOR_CANVAS_BG, getColor(R.color.colorCanvasBg, context))

        edit.putInt(ColorManager.COLOR_FACT_CARD_BG, getColor(R.color.colorFactCardBg, context))
        edit.putInt(
            ColorManager.COLOR_FACT_CARD_BORDER,
            getColor(R.color.colorFactCardBorder, context)
        )
        edit.putInt(ColorManager.COLOR_CARD_POINT, getColor(R.color.colorCardPoint, context))
        edit.putInt(
            ColorManager.COLOR_SELECTED_CARD_POINT,
            getColor(R.color.colorSelectedCardPoint, context)
        )
        edit.putInt(
            ColorManager.COLOR_FACT_CARD_TEXT,
            getColor(R.color.colorFactCardText, context)
        )

        edit.putInt(ColorManager.COLOR_LINE, getColor(R.color.colorLine, context))
        edit.putInt(
            ColorManager.COLOR_SELECTED_LINE,
            getColor(R.color.colorSelectedLine, context)
        )

        edit.apply()
    }

    private fun getColor(colorResId: Int, context: Context) =
        ContextCompat.getColor(context, colorResId)
}