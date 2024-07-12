package com.capstone.bloomy.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.capstone.bloomy.R
import com.capstone.bloomy.databinding.ActivityMainBinding
import com.capstone.bloomy.ui.fragment.DashboardFragment
import com.capstone.bloomy.ui.fragment.FishGradingFragment
import com.capstone.bloomy.ui.fragment.FishPricingFragment
import com.capstone.bloomy.ui.fragment.MarketFragment
import com.capstone.bloomy.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.button_home -> {
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.button_grading -> {
                    replaceFragment(FishGradingFragment())
                    true
                }
                R.id.button_market -> {
                    replaceFragment(MarketFragment())
                    true
                }
                R.id.button_pricing -> {
                    replaceFragment(FishPricingFragment())
                    true
                }
                R.id.button_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }

        if (intent.getBooleanExtra("navigateToProfileFragment", false)) {
            replaceFragment(ProfileFragment())
            binding.bottomNavigation.selectedItemId = R.id.button_profile
        } else {
            replaceFragment(DashboardFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }
}