package ru.dimagor555.factcard.ui.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import ru.dimagor555.factcard.R

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
        val prefs = preferenceManager.sharedPreferences ?: return
        SettingsResetter.resetAllColors(prefs, requireContext())
        findNavController().popBackStack()
        findNavController().navigate(R.id.settingsFragment)
    }
}