package ru.dimagor555.factcard.ui.settings

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import ru.dimagor555.factcard.R

class ResetPreference(context: Context?, attrs: AttributeSet?) : Preference(context, attrs) {
    constructor(context: Context?) : this(context, null)

    lateinit var resetBtnCallback: View.OnClickListener
    private lateinit var resetBtn: Button

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        holder?.let {
            resetBtn = it.itemView.findViewById(R.id.pref_button_reset)
            resetBtn.setOnClickListener(resetBtnCallback)
        }
    }

    companion object {
        const val RESET_PREF = "reset"
    }
}