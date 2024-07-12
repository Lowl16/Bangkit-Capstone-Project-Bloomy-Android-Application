package com.capstone.bloomy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.capstone.bloomy.R
import com.capstone.bloomy.databinding.ActivityTransactionBinding
import com.capstone.bloomy.ui.adapter.SectionPagerTransactionAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TransactionActivity : AppCompatActivity() {

    private lateinit var adapter: SectionPagerTransactionAdapter
    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolBarTransaction)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionPagerTransactionAdapter = SectionPagerTransactionAdapter(this)
        adapter = sectionPagerTransactionAdapter

        val viewPager: ViewPager2 = binding.viewPagerTransaction
        viewPager.adapter = sectionPagerTransactionAdapter

        val tabLayout: TabLayout = binding.tabLayoutTransaction
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToMainActivity()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navigateToMainActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("navigateToProfileFragment", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.purchases,
            R.string.sales
        )
    }
}