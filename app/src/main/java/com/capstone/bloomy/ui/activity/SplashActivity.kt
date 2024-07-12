package com.capstone.bloomy.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.capstone.bloomy.R
import com.capstone.bloomy.data.preferences.UserPreferences
import com.capstone.bloomy.data.preferences.dataStore
import com.capstone.bloomy.data.state.ResultState
import com.capstone.bloomy.ui.viewmodel.ProfileViewModel
import com.capstone.bloomy.ui.viewmodel.UserPreferencesViewModel
import com.capstone.bloomy.ui.viewmodelfactory.ProfileViewModelFactory
import com.capstone.bloomy.ui.viewmodelfactory.UserPreferencesViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashDuration: Long = 1500

    private var tokenInvalid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val userPreferences: UserPreferences = UserPreferences.getInstance(application.dataStore)
        val userPreferencesViewModelFactory: UserPreferencesViewModelFactory = UserPreferencesViewModelFactory.getInstance(userPreferences)
        val userPreferencesViewModel: UserPreferencesViewModel by viewModels { userPreferencesViewModelFactory }

        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, MainActivity::class.java)
            val homeIntent = Intent(this, HomeActivity::class.java)

            userPreferencesViewModel.getSession().observe(this) { session ->
                if (session != null) {
                    val profileViewModelFactory: ProfileViewModelFactory = ProfileViewModelFactory.getInstance(this@SplashActivity)
                    val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

                    profileViewModel.tokenInvalid()
                    profileViewModel.tokenInvalid().observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> { }

                                is ResultState.Success -> { }

                                is ResultState.Error -> {
                                    tokenInvalid = result.error
                                }
                            }
                        }
                    }
                    if (tokenInvalid != "Token invalid") {
                        Toast.makeText(this, getString(R.string.welcome_to_bloomy), Toast.LENGTH_SHORT).show()
                        startActivity(mainIntent)
                        finish()
                    } else {
                        startActivity(homeIntent)
                        finish()
                    }
                } else {
                    startActivity(homeIntent)
                    finish()
                }
            }
        }, splashDuration)
    }
}
