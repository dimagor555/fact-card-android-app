package ru.dimagor555.factcard.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.ui.drawfile.canvas.ColorManager

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        initResetBtn()
    }

    private fun initResetBtn() {
        findPreference<ResetPreference>(ResetPreference.RESET_PREF)?.resetBtnCallback =
            View.OnClickListener { resetAllColors() }
    }

    private fun resetAllColors() {
        val edit = preferenceManager.sharedPreferences.edit()
        edit.putInt(ColorManager.COLOR_CANVAS_BG, getColor(R.color.colorCanvasBg))

        edit.putInt(ColorManager.COLOR_FACT_CARD_BG, getColor(R.color.colorFactCardBg))
        edit.putInt(ColorManager.COLOR_FACT_CARD_BORDER, getColor(R.color.colorFactCardBorder))
        edit.putInt(ColorManager.COLOR_CARD_POINT, getColor(R.color.colorCardPoint))
        edit.putInt(ColorManager.COLOR_SELECTED_CARD_POINT, getColor(R.color.colorSelectedCardPoint))
        edit.putInt(ColorManager.COLOR_FACT_CARD_TEXT, getColor(R.color.colorFactCardText))

        edit.putInt(ColorManager.COLOR_LINE, getColor(R.color.colorLine))
        edit.putInt(ColorManager.COLOR_SELECTED_LINE, getColor(R.color.colorSelectedLine))

        edit.apply()
        findNavController().popBackStack()
        findNavController().navigate(R.id.settingsFragment)
    }

    private fun getColor(colorResId: Int) =
        ContextCompat.getColor(requireContext(), colorResId)
}