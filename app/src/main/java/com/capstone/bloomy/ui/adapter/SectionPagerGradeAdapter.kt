package com.capstone.bloomy.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.capstone.bloomy.ui.fragment.MarketGradeProductFragment

class SectionPagerGradeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = MarketGradeProductFragment()
        fragment.arguments = Bundle().apply {
            putInt(MarketGradeProductFragment.ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }
}