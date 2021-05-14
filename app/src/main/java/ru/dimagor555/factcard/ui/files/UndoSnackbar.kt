package ru.dimagor555.factcard.ui.files

import android.view.View
import com.google.android.material.snackbar.Snackbar
import ru.dimagor555.factcard.R

object UndoSnackbar {
    fun showUndoDeletingSnackbar(
        container: View,
        onUndoCallback: () -> Unit,
        onFinallyDeleteCallback: () -> Unit
    ) {
        val snackbar = Snackbar.make(container, R.string.file_deleted, Snackbar.LENGTH_LONG)
        snackbar.setAction(R.string.undo) { onUndoCallback() }
        snackbar.addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event != DISMISS_EVENT_ACTION)
                    onFinallyDeleteCallback()
            }
        })
        snackbar.show()
    }
}