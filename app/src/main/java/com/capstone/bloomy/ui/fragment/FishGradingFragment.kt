package com.capstone.bloomy.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.capstone.bloomy.R
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.FragmentFishGradingBinding
import com.capstone.bloomy.ui.viewmodel.FishGradingViewModel
import com.capstone.bloomy.ui.viewmodelfactory.FishGradingViewModelFactory
import com.capstone.bloomy.utils.getImageUri
import com.capstone.bloomy.utils.uriToFile

class FishGradingFragment : Fragment() {

    private var _binding: FragmentFishGradingBinding? = null
    private var imageUri: Uri? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFishGradingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCameraFishGrading.setOnClickListener {
            imageUri = getImageUri(requireContext())
            launchCamera.launch(imageUri)
        }

        binding.btnGalleryFishGrading.setOnClickListener {
            launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnGrade.setOnClickListener {
            imageUri?.let {
                showFishGradingDialog()
            } ?: Toast.makeText(context, getString(R.string.invalid_image_empty), Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showFishGradingDialog() {
        val fishGradingViewModelFactory: FishGradingViewModelFactory = FishGradingViewModelFactory.getInstance(requireContext())
        val fishGradingViewModel: FishGradingViewModel by viewModels { fishGradingViewModelFactory }

        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fish_grading_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val grade: TextView = dialog.findViewById(R.id.tv_fish_grading_dialog)
        val gradeDescription: TextView = dialog.findViewById(R.id.tv_description_fish_grading_dialog)
        val btnClose: Button = dialog.findViewById(R.id.btn_close_fish_grading_dialog)
        val progressBarFishGradingDialog: ProgressBar = dialog.findViewById(R.id.progress_bar_fish_grading_dialog)

        imageUri?.let { uri ->
            val gradeFish = binding.btnGrade
            val file = uriToFile(uri, requireContext())

            fishGradingViewModel.fishGrading(file).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoadingGrade(gradeFish, true)
                            grade.visibility = View.GONE
                            gradeDescription.visibility = View.GONE
                            btnClose.visibility = View.GONE
                            progressBarFishGradingDialog.visibility = View.VISIBLE
                        }

                        is ResultState.Success -> {
                            showLoadingGrade(gradeFish, false)
                            grade.visibility = View.VISIBLE
                            gradeDescription.visibility = View.VISIBLE
                            btnClose.visibility = View.VISIBLE
                            progressBarFishGradingDialog.visibility = View.GONE
                            Toast.makeText(context, result.data.fishGradingStatus.message, Toast.LENGTH_SHORT).show()

                            grade.text = when (result.data.fishGradingStatus.fishGradingData.fishClass) {
                                "Ikan" -> formatGradeIkan(result.data.fishGradingStatus.fishGradingData.fishGrade)
                                "Udang" -> formatGradeUdang(result.data.fishGradingStatus.fishGradingData.fishGrade)
                                else -> getString(R.string.default_grade)
                            }

                            gradeDescription.text =  if (result.data.fishGradingStatus.fishGradingData.fishClass == "Ikan") {
                                when (result.data.fishGradingStatus.fishGradingData.fishGrade) {
                                    "A" -> getString(R.string.fish_grading_description_a)
                                    "B" -> getString(R.string.fish_grading_description_b)
                                    "C" -> getString(R.string.fish_grading_description_c)
                                    else -> getString(R.string.default_fish_grading_description)
                                }
                            } else {
                                when (result.data.fishGradingStatus.fishGradingData.fishGrade) {
                                    "A" -> getString(R.string.shrimp_grading_description_a)
                                    "B" -> getString(R.string.shrimp_grading_description_b)
                                    "C" -> getString(R.string.shrimp_grading_description_c)
                                    else -> getString(R.string.default_fish_grading_description)
                                }
                            }
                        }

                        is ResultState.Error -> {
                            showLoadingGrade(gradeFish, false)
                            grade.visibility = View.VISIBLE
                            gradeDescription.visibility = View.VISIBLE
                            btnClose.visibility = View.VISIBLE
                            progressBarFishGradingDialog.visibility = View.GONE
                            Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private val launchCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            showImage()
        }
    }

    private val launchGallery = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            showImage()
        }
    }

    private fun showImage() {
        imageUri?.let {
            binding.imgFishGrading.setImageURI(it)
            binding.imgFishGrading.visibility = View.VISIBLE
            binding.imgChooseImage.visibility = View.GONE
            binding.tvChooseImage.visibility = View.GONE
        }
    }

    private fun formatGradeIkan(grade: String): String {
        return getString(R.string.fish_grade, grade)
    }

    private fun formatGradeUdang(grade: String): String {
        return getString(R.string.shrimp_grade, grade)
    }

    private fun showLoadingGrade(grade: Button, isLoading: Boolean) { grade.text = if (!isLoading) getString(R.string.btn_grade) else getString(R.string.btn_loading) }
}