package ru.dimagor555.factcard.ui.drawfile.canvas

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    val canvasBgColor
        get() = getColor(COLOR_CANVAS_BG)

    val factCardBgColor
        get() = getColor(COLOR_FACT_CARD_BG)
    val factCardBorderColor
        get() = getColor(COLOR_FACT_CARD_BORDER)
    val factCardPointColor
        get() = getColor(COLOR_CARD_POINT)
    val factCardSelectedPointColor
        get() = getColor(COLOR_SELECTED_CARD_POINT)
    val factCardTextColor
        get() = getColor(COLOR_FACT_CARD_TEXT)

    val lineColor
        get() = getColor(COLOR_LINE)
    val selectedLineColor
        get() = getColor(COLOR_SELECTED_LINE)

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private fun getColor(colorKey: String) = preferences.getInt(colorKey, 0)

    companion object {
        const val COLOR_CANVAS_BG = "colorCanvasBg"

        const val COLOR_FACT_CARD_BG = "colorFactCardBg"
        const val COLOR_FACT_CARD_BORDER = "colorFactCardBorder"
        const val COLOR_CARD_POINT = "colorCardPoint"
        const val COLOR_SELECTED_CARD_POINT = "colorSelectedCardPoint"
        const val COLOR_FACT_CARD_TEXT = "colorFactCardText"
        
        const val COLOR_LINE = "colorLine"
        const val COLOR_SELECTED_LINE = "colorSelectedLine"
    }
}