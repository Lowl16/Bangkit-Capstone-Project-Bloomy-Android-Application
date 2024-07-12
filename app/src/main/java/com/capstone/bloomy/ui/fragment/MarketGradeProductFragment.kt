package com.capstone.bloomy.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bloomy.data.response.ProductByGradeData
import com.capstone.bloomy.databinding.FragmentMarketGradeProductBinding
import com.capstone.bloomy.ui.adapter.MarketGradeProductAdapter
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory

class MarketGradeProductFragment : Fragment() {

    private var _binding: FragmentMarketGradeProductBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketGradeProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(requireContext())
        val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

        when (position) {
            1 -> {
                productViewModel.getProductByGrade("A")
                productViewModel.productByGrade.observe(viewLifecycleOwner) { gradeA ->
                    setProductByGrade(gradeA)
                }
            }
            2 -> {
                productViewModel.getProductByGrade("B")
                productViewModel.productByGrade.observe(viewLifecycleOwner) { gradeB ->
                    setProductByGrade(gradeB)
                }
            }
            else -> {
                productViewModel.getProductByGrade("C")
                productViewModel.productByGrade.observe(viewLifecycleOwner) { gradeC ->
                    setProductByGrade(gradeC)
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerViewMarketGradeProduct.layoutManager = layoutManager
    }

    private fun setProductByGrade(productByGradeData: List<ProductByGradeData>) {
        val adapter = MarketGradeProductAdapter()
        adapter.submitList(productByGradeData)
        binding.recyclerViewMarketGradeProduct.adapter = adapter
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}