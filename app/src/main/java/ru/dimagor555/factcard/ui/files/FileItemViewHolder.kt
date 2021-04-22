package ru.dimagor555.factcard.ui.files

import androidx.recyclerview.widget.RecyclerView
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.databinding.ListItemFileBinding

class FileItemViewHolder(
    private val binding: ListItemFileBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(file: File) {
        binding.file = file
        binding.executePendingBindings()
    }
}