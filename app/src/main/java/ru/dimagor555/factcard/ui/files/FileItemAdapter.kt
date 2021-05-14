package ru.dimagor555.factcard.ui.files

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.databinding.ListItemFileBinding

class FileItemAdapter(
    private val onShowUndoSnackBar: (file: File, onUndoCallback: () -> Unit) -> Unit
) : RecyclerView.Adapter<FileItemViewHolder>() {
    private var files: MutableList<File> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemFileBinding.inflate(inflater, parent, false)
        return FileItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FileItemViewHolder, position: Int) {
        holder.bind(files[position])
    }

    override fun getItemCount() = files.size

    fun setData(files: List<File>) {
        this.files = files as MutableList<File>
        notifyDataSetChanged()
    }

    fun removeAt(index: Int) {
        val deletedFile = files.removeAt(index)
        notifyItemRemoved(index)
        onShowUndoSnackBar(deletedFile) { undoDeleting(index, deletedFile) }
    }

    private fun undoDeleting(index: Int, deletedFile: File) {
        files.add(index, deletedFile)
        notifyDataSetChanged()
    }
}