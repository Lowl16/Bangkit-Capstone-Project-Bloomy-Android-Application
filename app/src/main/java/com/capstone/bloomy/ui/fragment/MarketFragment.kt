package com.capstone.bloomy.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.capstone.bloomy.R
import com.capstone.bloomy.data.response.ProductData
import com.capstone.bloomy.databinding.FragmentMarketBinding
import com.capstone.bloomy.ui.activity.MarketSearchProductActivity
import com.capstone.bloomy.ui.adapter.FreshCatchMarketAdapter
import com.capstone.bloomy.ui.adapter.SectionPagerGradeAdapter
import com.capstone.bloomy.ui.viewmodel.ProductViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProductViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MarketFragment : Fragment() {

    private lateinit var adapter: SectionPagerGradeAdapter

    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            searchViewMarket.setupWithSearchBar(searchBarMarket)
            searchViewMarket
                .editText
                .setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        val query = searchViewMarket.text.toString()
                        searchViewMarket.hide()

                        if (query.isNotEmpty()) { handleSearch(query) }

                        return@setOnEditorActionListener true
                    }
                    false
                }
        }

        val productViewModelFactory: ProductViewModelFactory = ProductViewModelFactory.getInstance(requireContext())
        val productViewModel: ProductViewModel by viewModels { productViewModelFactory }

        productViewModel.getProduct()
        productViewModel.products.observe(viewLifecycleOwner) { products ->
            setProduct(products)
        }

        val sectionPagerGradeAdapter = SectionPagerGradeAdapter(this)
        adapter = sectionPagerGradeAdapter

        val viewPager: ViewPager2 = binding.viewPagerGradeMarket
        viewPager.adapter = sectionPagerGradeAdapter

        val tabLayout: TabLayout = binding.tabLayoutGradeMarket
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setProduct(product: List<ProductData>) {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewFreshCatchMarket.layoutManager = layoutManager

        val adapter = FreshCatchMarketAdapter()
        adapter.submitList(product)
        binding.recyclerViewFreshCatchMarket.adapter = adapter
    }

    private fun handleSearch(query: String) {
        val intent = Intent(requireContext(), MarketSearchProductActivity::class.java)
        intent.putExtra("search_query", query)
        startActivity(intent)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.grade_a,
            R.string.grade_b,
            R.string.grade_c
        )
    }
}