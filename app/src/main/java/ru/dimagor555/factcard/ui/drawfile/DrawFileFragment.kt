package ru.dimagor555.factcard.ui.drawfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.dimagor555.factcard.databinding.FragmentDrawFileBinding
import ru.dimagor555.factcard.ui.KeyboardUtils
import ru.dimagor555.factcard.ui.drawfile.canvas.CanvasMode

@AndroidEntryPoint
class DrawFileFragment : Fragment() {
    private lateinit var binding: FragmentDrawFileBinding
    private val viewModel by viewModels<DrawFileViewModel>()
    private val args by navArgs<DrawFileFragmentArgs>()

    private lateinit var menuManager: DrawMenuManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrawFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initToolbar()
        initCanvas()
        bindViewModel()
        setupBackButtonListener()
    }

    private fun initToolbar() {
        menuManager = DrawMenuManager(binding.fragDrawFileToolbar)
        menuManager.fileCanvas = viewModel.fileCanvas
        menuManager.currMode = CanvasMode.NOTHING_SELECTED
        viewModel.fileCanvas.mode.observe(viewLifecycleOwner, {
            menuManager.currMode = it
        })
        menuManager.onApplyCardTextEditingCallback = Runnable { applyCardTextEditing() }
        menuManager.onCancelCardTextEditingCallback = Runnable { cancelCardTextEditing() }
    }

    private fun initCanvas() {
        val canvas = binding.fragDrawFileCanvas
        canvas.fileCanvas = viewModel.fileCanvas
    }

    private fun bindViewModel() {
        viewModel.setFileId(args.fileName)
        viewModel.fileCanvas.mode.observe(viewLifecycleOwner, {
            if (it == CanvasMode.CARD_TEXT_EDITING) {
                binding.fragDrawFileCardTextLayout.visibility = View.VISIBLE
                binding.fragDrawFileCardText.setText(viewModel.fileCanvas.selectedCardText)
                binding.fragDrawFileCardText.requestFocus()
                context?.let { context ->
                    KeyboardUtils.showKeyboard(context, binding.fragDrawFileCardText)
                }
            } else
                binding.fragDrawFileCardTextLayout.visibility = View.GONE
        })
    }

    private fun applyCardTextEditing() {
        val text = binding.fragDrawFileCardText.text.toString()
        viewModel.fileCanvas.finishEditingText(text)
        context?.let { KeyboardUtils.hideKeyboard(it, binding.fragDrawFileCanvas.windowToken) }
    }

    private fun cancelCardTextEditing() {
        viewModel.fileCanvas.finishEditingText(null)
        context?.let { KeyboardUtils.hideKeyboard(it, binding.fragDrawFileCanvas.windowToken) }
    }

    private fun setupBackButtonListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (viewModel.fileCanvas.mode.value == CanvasMode.NOTHING_SELECTED)
                this@DrawFileFragment.findNavController().popBackStack()
            else
                viewModel.fileCanvas.selectObject(null)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.fragDrawFileCanvas.invalidate()
    }
}