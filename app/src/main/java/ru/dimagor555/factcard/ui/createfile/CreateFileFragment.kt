package ru.dimagor555.factcard.ui.createfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import ru.dimagor555.factcard.R
import ru.dimagor555.factcard.databinding.FragmentCreateFileBinding

@AndroidEntryPoint
class CreateFileFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(CreateFileViewModel::class.java)
    }

    private lateinit var bindings: FragmentCreateFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindings = FragmentCreateFileBinding.inflate(inflater)
        return bindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initButton()
        bindViewModel()
    }

    private fun initButton() {
        bindings.fragCreateFileBtnCreate.setOnClickListener {
            if (!bindings.fragCreateFileTextInputName.text.isNullOrEmpty()) {
                val name = bindings.fragCreateFileTextInputName.text.toString().trim()
                showError(null)
                viewModel.onClickCreate(name)
            } else
                showError(R.string.file_name_empty_error)
        }
    }

    private fun bindViewModel() {
        viewModel.fileId.observe(viewLifecycleOwner, {
            it?.let {
                val navDirection = CreateFileFragmentDirections
                    .actionCreateFileFragmentToDrawFileFragment(it)
                Navigation.findNavController(bindings.root)
                    .navigate(navDirection)
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            it?.let {
                showError(it)
            }
        })
    }

    private fun showError(stringResId: Int?) {
        var error: String? = null
        stringResId?.let {
            error = getString(stringResId)
        }
        bindings.fragCreateFileTextInputLayoutName.error = error
    }
}