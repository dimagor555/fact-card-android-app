package ru.dimagor555.factcard.ui.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.ui.drawfile.canvas.ColorManager
import androidx.core.content.edit

object SettingsResetter {
    fun resetAllColors(sharedPreferences: SharedPreferences, context: Context) {
        sharedPreferences.edit {
            putInt(ColorManager.COLOR_CANVAS_BG, getColor(R.color.colorCanvasBg, context))

            putInt(ColorManager.COLOR_FACT_CARD_BG, getColor(R.color.colorFactCardBg, context))
            putInt(
                ColorManager.COLOR_FACT_CARD_BORDER,
                getColor(R.color.colorFactCardBorder, context)
            )
            putInt(ColorManager.COLOR_CARD_POINT, getColor(R.color.colorCardPoint, context))
            putInt(
                ColorManager.COLOR_SELECTED_CARD_POINT,
                getColor(R.color.colorSelectedCardPoint, context)
            )
            putInt(
                ColorManager.COLOR_FACT_CARD_TEXT,
                getColor(R.color.colorFactCardText, context)
            )

            putInt(ColorManager.COLOR_LINE, getColor(R.color.colorLine, context))
            putInt(
                ColorManager.COLOR_SELECTED_LINE,
                getColor(R.color.colorSelectedLine, context)
            )
        }
    }

    private fun getColor(colorResId: Int, context: Context) =
        ContextCompat.getColor(context, colorResId)
}