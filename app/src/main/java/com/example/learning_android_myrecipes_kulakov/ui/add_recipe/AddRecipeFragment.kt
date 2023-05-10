package com.example.learning_android_myrecipes_kulakov.ui.add_recipe

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learning_android_myrecipes_kulakov.R
import com.example.learning_android_myrecipes_kulakov.Utils.disable
import com.example.learning_android_myrecipes_kulakov.databinding.FragmentAddRecipeBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddRecipeFragment : Fragment(), View.OnClickListener, DialogInterface.OnClickListener, OnTimeSetListener {

    private lateinit var binding: FragmentAddRecipeBinding

    private val viewModel by viewModels<AddRecipeViewModel>()

    private lateinit var uri: Uri

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            viewModel.saveImage(uri)
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val uri = it.data?.data ?: return@registerForActivityResult
            viewModel.saveImage(uri)
        }
    }

    private var editMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val addMode = requireArguments().getLong(ID) < 0
        editMode = requireArguments().getBoolean(EDIT, false) && !addMode

        binding.btnSave.isVisible = editMode || addMode

        if (!editMode && !addMode) {
            binding.etName.disable()
            binding.etCategory.isClickable = false
            binding.etCategory.isFocusable = false
            binding.tlCategory.endIconMode = TextInputLayout.END_ICON_NONE
            binding.etDescription.disable()
            binding.chipAddIngredient.isVisible = false
        }

        if (editMode || addMode) {
            binding.btnSave.setOnClickListener(this)
            binding.ivPhoto.setOnClickListener(this)
            binding.etTime.setOnClickListener(this)
            binding.chipAddIngredient.setOnClickListener(this)
        }

        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finish.collectLatest {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivPhoto -> {
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.select_photo)
                    .setItems(R.array.select_photo, this)
                    .show()
            }
            binding.etTime -> {
                TimePickerDialog(requireContext(), this,
                    viewModel.calendar.get(Calendar.HOUR_OF_DAY),
                    viewModel.calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }
            binding.chipAddIngredient -> {
                val editText = EditText(requireContext())
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.add)
                    .setView(editText)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        val text = editText.text?.toString()?.trim()
                        if (!editText.text?.toString().isNullOrBlank())
                            viewModel.addIngredient(text.orEmpty())
                    }
                    .show()
            }
        }
    }

    override fun onClick(p0: DialogInterface?, position: Int) {
        when (position) {
            0 -> openCamera()
            1 -> openGallery()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(System.currentTimeMillis())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDir)
        uri = FileProvider.getUriForFile(requireContext(), "com.example.learning_android_myrecipes_kulakov.fileprovider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        cameraLauncher.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {
        viewModel.setTime(hours, minutes)
    }

    companion object {
        const val ID = "ID"
        const val EDIT = "EDIT"
        fun createInstance(id: Long = 0, edit: Boolean = false) : AddRecipeFragment {
            val fragment = AddRecipeFragment()
            fragment.arguments = bundleOf(
                ID to id,
                EDIT to edit
            )
            return fragment
        }
    }
}