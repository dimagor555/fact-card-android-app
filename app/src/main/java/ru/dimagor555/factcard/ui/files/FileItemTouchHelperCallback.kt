package ru.dimagor555.factcard.ui.files

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class FileItemTouchHelperCallback(
    private val listener: OnSwipeListener
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) =
        listener.onSwipe(viewHolder.adapterPosition)

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            val foregroundView = getForegroundViewFromViewHolder(viewHolder)
            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView = getForegroundViewFromViewHolder(viewHolder)
        getDefaultUIUtil().clearView(foregroundView)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val foregroundView = getForegroundViewFromViewHolder(viewHolder)
        getDefaultUIUtil().onDraw(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        viewHolder?.let {
            val foregroundView = getForegroundViewFromViewHolder(viewHolder)
            getDefaultUIUtil().onDrawOver(
                c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive
            )
        }
    }

    private fun getForegroundViewFromViewHolder(viewHolder: RecyclerView.ViewHolder) =
        (viewHolder as FileItemViewHolder).viewForeground

    interface OnSwipeListener {
        fun onSwipe(position: Int)
    }
}