package ru.dimagor555.factcard.ui.drawfile

import androidx.appcompat.widget.Toolbar
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.ui.drawfile.canvas.CanvasMode
import ru.dimagor555.factcard.ui.drawfile.canvas.FileCanvas

class DrawMenuManager(private val toolbar: Toolbar) {
    lateinit var fileCanvas: FileCanvas
    lateinit var onApplyCardTextEditingCallback: Runnable
    lateinit var onCancelCardTextEditingCallback: Runnable

    var currMode: CanvasMode = CanvasMode.NOTHING_SELECTED
        set(value) {
            field = value
            toolbar.menu.clear()
            getMenuResIdForMode(field)?.let { toolbar.inflateMenu(it) }
            setMenuListener()
        }

    private fun setMenuListener() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_create_fact_card -> fileCanvas.createFactCardInCenter()
                R.id.action_save_canvas_image -> fileCanvas.saveCanvasImageToGallery()

                R.id.action_delete_fact_card -> fileCanvas.deleteSelectedFactCard()
                R.id.action_increase_card_font_size ->
                    fileCanvas.increaseSelectedFactCardTextSize()
                R.id.action_decrease_card_font_size ->
                    fileCanvas.decreaseSelectedFactCardTextSize()
                R.id.action_edit_card_text -> fileCanvas.startEditingCardText()
                R.id.action_apply_edited_card_text -> onApplyCardTextEditingCallback.run()
                R.id.action_cancel_card_text_editing -> onCancelCardTextEditingCallback.run()

                R.id.action_delete_line -> fileCanvas.deleteSelectedLine()

                R.id.action_cancel_line_selection, R.id.action_cancel_card_selection ->
                    fileCanvas.selectObject(null)
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun getMenuResIdForMode(mode: CanvasMode) =
        when (mode) {
            CanvasMode.NOTHING_SELECTED -> R.menu.menu_frag_draw_nothing_selected
            CanvasMode.CARD_SELECTED -> R.menu.menu_frag_draw_card_selected
            CanvasMode.LINE_SELECTED -> R.menu.menu_frag_draw_line_selected
            CanvasMode.CARD_TEXT_EDITING -> R.menu.menu_frag_draw_card_text_editing
            CanvasMode.LINE_CREATING -> null
        }
}