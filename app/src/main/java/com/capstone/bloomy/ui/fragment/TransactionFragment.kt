package com.capstone.bloomy.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.bloomy.data.response.PurchasesTransactionData
import com.capstone.bloomy.data.response.SalesTransactionData
import com.capstone.bloomy.databinding.FragmentTransactionBinding
import com.capstone.bloomy.ui.adapter.TransactionPurchasesAdapter
import com.capstone.bloomy.ui.adapter.TransactionSalesAdapter
import com.capstone.bloomy.ui.viewmodel.TransactionViewModel
import com.capstone.bloomy.ui.viewmodelfactory.TransactionViewModelFactory

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val transactionViewModelFactory: TransactionViewModelFactory = TransactionViewModelFactory.getInstance(requireContext())
        val transactionViewModel: TransactionViewModel by viewModels { transactionViewModelFactory }

        when (position) {
            1 -> {
                transactionViewModel.getPurchasesTransaction()
                transactionViewModel.purchasesTransaction.observe(viewLifecycleOwner) { purchases ->
                    if (purchases != null) {
                        setPurchasesTransaction(purchases)
                        binding.tvInvalidTransaction.visibility = View.GONE
                    }
                }
            }
            2 -> {
                transactionViewModel.getSalesTransaction()
                transactionViewModel.salesTransaction.observe(viewLifecycleOwner) { sales ->
                    if (sales != null) {
                        setSalesTransaction(sales)
                        binding.tvInvalidTransaction.visibility = View.GONE
                    }
                }
            }
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerViewTransaction.layoutManager = layoutManager
    }

    private fun setSalesTransaction(salesTransactionData: List<SalesTransactionData>) {
        val adapter = TransactionSalesAdapter()
        adapter.submitList(salesTransactionData)
        binding.recyclerViewTransaction.adapter = adapter
    }

    private fun setPurchasesTransaction(purchasesTransactionData: List<PurchasesTransactionData>) {
        val adapter = TransactionPurchasesAdapter()
        adapter.submitList(purchasesTransactionData)
        binding.recyclerViewTransaction.adapter = adapter
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
    }
}