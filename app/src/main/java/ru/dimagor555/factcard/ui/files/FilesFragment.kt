package ru.dimagor555.factcard.ui.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import dagger.hilt.android.AndroidEntryPoint
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.data.file.File
import ru.dimagor555.factcard.databinding.FragmentFilesBinding

@AndroidEntryPoint
class FilesFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(FilesViewModel::class.java)
    }

    private lateinit var binding: FragmentFilesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        binding.fragFilesToolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.action_create_file -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_filesFragment_to_createFileFragment)
                    true
                }
                R.id.action_settings -> {
                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_filesFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun initRecyclerView() {
        val adapter = initFileItemAdapter()
        binding.fragFilesRecyclerView.adapter = adapter

        viewModel.allFiles.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }

    private fun initFileItemAdapter(): FileItemAdapter {
        val adapter =
            FileItemAdapter { file: File, onUndoCallback: () -> Unit ->
                showUndoDeletingSnackbar(file, onUndoCallback)
            }

        ItemTouchHelper(FileItemTouchHelperCallback(object :
            FileItemTouchHelperCallback.OnSwipeListener {
            override fun onSwipe(position: Int) {
                adapter.removeAt(position)
            }
        }))
            .attachToRecyclerView(binding.fragFilesRecyclerView)

        return adapter
    }

    private fun showUndoDeletingSnackbar(file: File, onUndoCallback: () -> Unit) {
        UndoSnackbar.showUndoDeletingSnackbar(
            binding.fragFilesCoordinatorLayout,
            onUndoCallback
        ) {
            viewModel.deleteFile(file)
        }
    }
}