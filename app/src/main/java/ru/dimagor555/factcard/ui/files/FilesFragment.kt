package ru.dimagor555.factcard.ui.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import ru.dimagor555.factcard.R
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
    ): View? {
        binding = FragmentFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        binding.fragFilesToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_create_file) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_filesFragment_to_createFileFragment)
                return@setOnMenuItemClickListener true
            } else
                return@setOnMenuItemClickListener false
        }
    }

    private fun initRecyclerView() {
        val adapter = FileItemAdapter()
        binding.fragFilesRecyclerView.adapter = adapter
        viewModel.allFiles.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }
}