package com.capstone.bloomy.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.capstone.bloomy.R
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.databinding.FragmentFishPricingBinding
import com.capstone.bloomy.ui.viewmodel.FishPricingViewModel
import com.capstone.bloomy.ui.viewmodel.FishViewModel
import com.capstone.bloomy.ui.viewmodelfactory.FishPricingViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.FishViewModelFactory

class FishPricingFragment : Fragment() {

    private var _binding: FragmentFishPricingBinding? = null
    private var listFishName = ArrayList<String>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFishPricingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fishViewModelFactory: FishViewModelFactory = FishViewModelFactory.getInstance(requireContext())
        val fishViewModel: FishViewModel by viewModels { fishViewModelFactory }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listFishName)
        binding.etSpeciesFishPricing.setAdapter(adapter)

        binding.etSpeciesFishPricing.setOnItemClickListener { parent, _, position, _ ->
            val selectedFish = parent.getItemAtPosition(position).toString()
            val selectedFishData = fishViewModel.fish.value?.find { it.nama == selectedFish }

            selectedFishData?.let {
                val formattedPrice = formatCurrency(it.price) + "/kg"
                binding.etActualPriceFishPricing.text = Editable.Factory.getInstance().newEditable(formattedPrice)
            }
        }

        fishViewModel.getFish()
        fishViewModel.fish.observe(viewLifecycleOwner) { fish ->
            adapter.clear()
            adapter.addAll(fish.map { it.nama })
        }

        binding.btnPredict.setOnClickListener {
            val species = binding.etSpeciesFishPricing.text.toString()
            val actualPrice = binding.etActualPriceFishPricing.text.toString()
            val catchingMethod = binding.etCatchingMethodFishPricing.text.toString()
            val grade = binding.etGradeFishPricing.text.toString()
            val sustainability = binding.etSustainabilityFishPricing.text.toString()
            val weight = binding.etWeightFishPricing.text.toString()

            if (species.isNotEmpty() && actualPrice.isNotEmpty() && catchingMethod.isNotEmpty() && grade.isNotEmpty() && sustainability.isNotEmpty() && weight.isNotEmpty()) {
                showFishPricingDialog()
            } else {
                Toast.makeText(requireContext(), getString(R.string.invalid_input), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val catchingMethod = resources.getStringArray(R.array.value_catching_method)
        val catchingMethodArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_list_catching_method, catchingMethod)
        val grade = resources.getStringArray(R.array.value_grade)
        val gradeArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_list_grade, grade)
        val sustainability = resources.getStringArray(R.array.value_sustainability)
        val sustainabilityArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_list_sustainability, sustainability)

        binding.etCatchingMethodFishPricing.setAdapter(catchingMethodArrayAdapter)
        binding.etGradeFishPricing.setAdapter(gradeArrayAdapter)
        binding.etSustainabilityFishPricing.setAdapter(sustainabilityArrayAdapter)
    }

    private fun showFishPricingDialog() {
        val fishPricingViewModelFactory: FishPricingViewModelFactory = FishPricingViewModelFactory.getInstance(requireContext())
        val fishPricingViewModel: FishPricingViewModel by viewModels { fishPricingViewModelFactory }

        val dialog = Dialog(requireContext())

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fish_pricing_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val price: TextView = dialog.findViewById(R.id.tv_fish_pricing_dialog)
        val priceValue: TextView = dialog.findViewById(R.id.tv_description_fish_pricing_dialog)
        val btnClose: Button = dialog.findViewById(R.id.btn_close_fish_pricing_dialog)
        val progressBarFishPricingDialog: ProgressBar = dialog.findViewById(R.id.progress_bar_fish_pricing_dialog)

        val priceFish = binding.btnPredict
        val gradeValue = getGradeValue(binding.etGradeFishPricing.text.toString()).toFloat()
        val catchingMethodValue = getCatchingMethodValue(binding.etCatchingMethodFishPricing.text.toString()).toFloat()
        val sustainabilityValue = getSustainabilityValue(binding.etSustainabilityFishPricing.text.toString()).toFloat()
        val actualPriceValue = unformatCurrency(binding.etActualPriceFishPricing.text.toString()).toFloat()

        fishPricingViewModel.fishPricing(gradeValue, catchingMethodValue, sustainabilityValue, actualPriceValue).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoadingPrice(priceFish, true)
                        price.visibility = View.GONE
                        priceValue.visibility = View.GONE
                        btnClose.visibility = View.GONE
                        progressBarFishPricingDialog.visibility = View.VISIBLE
                    }

                    is ResultState.Success -> {
                        showLoadingPrice(priceFish, false)
                        price.visibility = View.VISIBLE
                        priceValue.visibility = View.VISIBLE
                        btnClose.visibility = View.VISIBLE
                        progressBarFishPricingDialog.visibility = View.GONE
                        Toast.makeText(context, result.data.fishPricingStatus.message, Toast.LENGTH_SHORT).show()

                        val priceValueNumeric = result.data.fishPricingStatus.fishPricingData.price
                        val weightValue = binding.etWeightFishPricing.text.toString().toFloat()
                        val recommendedPrice = priceValueNumeric * weightValue

                        priceValue.text = formatCurrency(recommendedPrice.toInt())
                    }

                    is ResultState.Error -> {
                        showLoadingPrice(priceFish, false)
                        price.visibility = View.VISIBLE
                        priceValue.visibility = View.VISIBLE
                        btnClose.visibility = View.VISIBLE
                        progressBarFishPricingDialog.visibility = View.GONE
                        Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getGradeValue(gradeText: String): Int {
        return when (gradeText) {
            "A" -> 2
            "B" -> 1
            "C" -> 0
            else -> 0
        }
    }

    private fun getCatchingMethodValue(catchingMethodText: String): Int {
        return when (catchingMethodText) {
            "Farmed" -> 3
            "Netting" -> 2
            "Spearfishing" -> 0
            "Fishing Hook" -> 1
            else -> 0
        }
    }

    private fun getSustainabilityValue(sustainabilityText: String): Int {
        return when (sustainabilityText) {
            "Moderately Sustainable" -> 1
            "Unsustainable" -> 0
            else -> 0
        }
    }

    private fun formatCurrency(amount: Int): String {
        val formattedAmount = String.format("Rp%,d", amount)
        return formattedAmount.replace(',', '.')
    }

    private fun unformatCurrency(formattedAmount: String): Int {
        val unformattedAmount = formattedAmount.replace("[^\\d]".toRegex(), "")
        return if (unformattedAmount.isNotEmpty()) {
            unformattedAmount.toInt()
        } else {
            0
        }
    }

    private fun showLoadingPrice(price: Button, isLoading: Boolean) { price.text = if (!isLoading) getString(R.string.btn_predict) else getString(R.string.btn_loading) }
}